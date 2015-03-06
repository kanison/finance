package com.tenpay.sm.lang.lb.hessian;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.RemoteLookupFailureException;
import org.springframework.remoting.RemoteProxyFailureException;
import org.springframework.remoting.support.UrlBasedRemoteAccessor;

import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.client.HessianRuntimeException;
import com.caucho.hessian.io.SerializerFactory;
import com.tenpay.sm.lang.config.ReloadableAppConfig;
import com.tenpay.sm.lang.lb.DefaultLoadbalanceStrategy;
import com.tenpay.sm.lang.lb.InvokeResult;
import com.tenpay.sm.lang.lb.InvokeTagetInfo;
import com.tenpay.sm.lang.lb.LoadbalanceStrategy;

/**
 * 实现负载均衡的hessian调用
 * 
 * @author aixxia
 * 
 */
public class HessianClientInterceptorLB extends UrlBasedRemoteAccessor
		implements MethodInterceptor {

	private static final Logger LOG = Logger
			.getLogger(HessianClientInterceptorLB.class);

	private HessianProxyFactory proxyFactory = new HessianProxyFactory();

	private LoadbalanceStrategy loadbalanceStrategy;

	public HessianClientInterceptorLB() {
		this.loadbalanceStrategy = new DefaultLoadbalanceStrategy();
	}

	/**
	 * 创建hessianProxy对象列表 属性设置时触发
	 */
	public void prepare() throws RemoteLookupFailureException {
		try {
			String urls = StringUtils.trimToNull(getServiceUrl());
			if (urls == null) {// 空值判断
				throw new RuntimeException("ServiceUrl url not set...");
			}
			List<Object> targets = new ArrayList<Object>();
			List<Object> hotBackupTargetsWithSwitchBack = new ArrayList<Object>();
			List<Object> hotBackupTargets = new ArrayList<Object>();
			if (LOG.isInfoEnabled()) {
				LOG.info("start init HessianProxy:" + urls);
			}
			for (String url : urls.split(";")) {
				// 逐个创建hessianProxy
				url = StringUtils.trimToNull(url);
				if (url != null) {
					String cmd = null;
					int beginIndex = url.indexOf("(+");
					if (beginIndex >= 0) {
						int endIndex = url.indexOf(')', beginIndex + 2);
						if (endIndex >= 0) {
							cmd = url.substring(beginIndex + 2, endIndex)
									.trim();
							url = StringUtils.trimToNull(url.substring(0,
									beginIndex)
									+ url.substring(endIndex + 1));
							if (url == null)
								continue;
						}
					}
					InvokeTagetInfo targetInfo = new InvokeTagetInfo(
							proxyFactory.create(getServiceInterface(), url),
							url);
					if (cmd != null) {
						if (cmd.equalsIgnoreCase("H")) {
							hotBackupTargetsWithSwitchBack.add(targetInfo);
						} else if (cmd.equalsIgnoreCase("B")) {
							hotBackupTargets.add(targetInfo);
						}
					} else
						targets.add(targetInfo);
				} else {
					LOG.warn("with null url:" + urls);
				}
			}
			loadbalanceStrategy.setTargets(targets);
			if (hotBackupTargetsWithSwitchBack.size() > 0)
				loadbalanceStrategy
						.setHotBackupTargetsWithSwitchBack(hotBackupTargetsWithSwitchBack);
			if (hotBackupTargets.size() > 0)
				loadbalanceStrategy.setHotBackupTargets(hotBackupTargets);
			loadbalanceStrategy.init();
			LOG.info(targets.toString()
					+ hotBackupTargetsWithSwitchBack.toString()
					+ hotBackupTargets.toString());

		} catch (Throwable ex) {
			throw new RemoteLookupFailureException("Service URL ["
					+ getServiceUrl() + "] is invalid", ex);
		}
	}

	/**
	 * 调用入口
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		InvokeTagetInfo tag = loadbalanceStrategy.getOne(null);
		try {
			return invokeAndRecord(invocation, tag);
		} catch (Throwable e) {
			String lbRetry = ReloadableAppConfig.appConfig
					.get("hessian.lbRetry");
			if (lbRetry == null
					|| (lbRetry.equals("1") == false && lbRetry
							.equalsIgnoreCase("true") == false))
				throw e;
			// 重试1次
			LOG.warn("retry invoke...");
			tag = loadbalanceStrategy.getOne(tag);
			return invokeAndRecord(invocation, tag);
		}
	}

	/**
	 * 调用并且记录错误
	 * 
	 * @param invocation
	 * @return
	 * @throws Throwable
	 */
	private Object invokeAndRecord(MethodInvocation invocation,
			InvokeTagetInfo tag) throws Throwable {
		long start = new Date().getTime();
		Throwable error = null;
		boolean success = true;
		try {
			return invoke(invocation, tag);
		} catch (Throwable e) {// 标记错误并且获取异常
			success = false;
			error = e;
			throw e;
		} finally {
			long end = new Date().getTime();
			long cost = end - start;
			InvokeResult ir = new InvokeResult();
			ir.setError(error);
			ir.setSuccess(success);
			ir.setTarget(tag);
			ir.setTimeCost(cost);
			loadbalanceStrategy.uploadInvokeResult(ir);
		}
	}

	/**
	 * 调用的具体实现
	 */
	private Object invoke(MethodInvocation invocation, InvokeTagetInfo target)
			throws Throwable {
		ClassLoader originalClassLoader = overrideThreadContextClassLoader();
		try {
			// 修改点 通过target取得调用目标
			return invocation.getMethod().invoke(target.getTarget(),
					invocation.getArguments());
		} catch (InvocationTargetException ex) {
			if (ex.getTargetException() instanceof HessianRuntimeException) {
				HessianRuntimeException hre = (HessianRuntimeException) ex
						.getTargetException();
				Throwable rootCause = (hre.getRootCause() != null ? hre
						.getRootCause() : hre);
				throw convertHessianAccessException(rootCause, target);
			} else if (ex.getTargetException() instanceof UndeclaredThrowableException) {
				UndeclaredThrowableException utex = (UndeclaredThrowableException) ex
						.getTargetException();
				throw convertHessianAccessException(utex
						.getUndeclaredThrowable(), target);
			}
			throw ex.getTargetException();
		} catch (Throwable ex) {
			throw new RemoteProxyFailureException(
					"Failed to invoke Hessian proxy for remote service ["
							+ target.getUrl() + "]", ex);
		} finally {
			resetThreadContextClassLoader(originalClassLoader);
		}
	}

	public void setLoadbalanceStrategy(LoadbalanceStrategy loadbalanceStrategy) {
		this.loadbalanceStrategy = loadbalanceStrategy;
	}

	// 以下为原来实现代码

	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		prepare();
	}

	/**
	 * Set the HessianProxyFactory instance to use. If not specified, a default
	 * HessianProxyFactory will be created.
	 * <p>
	 * Allows to use an externally configured factory instance, in particular a
	 * custom HessianProxyFactory subclass.
	 */
	public void setProxyFactory(HessianProxyFactory proxyFactory) {
		this.proxyFactory = (proxyFactory != null ? proxyFactory
				: new HessianProxyFactory());
	}

	/**
	 * Specify the Hessian SerializerFactory to use.
	 * <p>
	 * This will typically be passed in as an inner bean definition of type
	 * <code>com.caucho.hessian.io.SerializerFactory</code>, with custom bean
	 * property values applied.
	 */
	public void setSerializerFactory(SerializerFactory serializerFactory) {
		this.proxyFactory.setSerializerFactory(serializerFactory);
	}

	/**
	 * Set whether to send the Java collection type for each serialized
	 * collection. Default is "true".
	 */
	public void setSendCollectionType(boolean sendCollectionType) {
		this.proxyFactory.getSerializerFactory().setSendCollectionType(
				sendCollectionType);
	}

	/**
	 * Set whether overloaded methods should be enabled for remote invocations.
	 * Default is "false".
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setOverloadEnabled
	 */
	public void setOverloadEnabled(boolean overloadEnabled) {
		this.proxyFactory.setOverloadEnabled(overloadEnabled);
	}

	/**
	 * Set the username that this factory should use to access the remote
	 * service. Default is none.
	 * <p>
	 * The username will be sent by Hessian via HTTP Basic Authentication.
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setUser
	 */
	public void setUsername(String username) {
		this.proxyFactory.setUser(username);
	}

	/**
	 * Set the password that this factory should use to access the remote
	 * service. Default is none.
	 * <p>
	 * The password will be sent by Hessian via HTTP Basic Authentication.
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setPassword
	 */
	public void setPassword(String password) {
		this.proxyFactory.setPassword(password);
	}

	/**
	 * Set whether Hessian's debug mode should be enabled. Default is "false".
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setDebug
	 */
	public void setDebug(boolean debug) {
		this.proxyFactory.setDebug(debug);
	}

	/**
	 * Set whether to use a chunked post for sending a Hessian request.
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setChunkedPost
	 */
	public void setChunkedPost(boolean chunkedPost) {
		this.proxyFactory.setChunkedPost(chunkedPost);
	}

	/**
	 * Set the timeout to use when waiting for a reply from the Hessian service.
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setReadTimeout
	 */
	public void setReadTimeout(long timeout) {
		this.proxyFactory.setReadTimeout(timeout);
	}

	/**
	 * Set whether version 2 of the Hessian protocol should be used for parsing
	 * requests and replies. Default is "false".
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setHessian2Request
	 */
	public void setHessian2(boolean hessian2) {
		this.proxyFactory.setHessian2Request(hessian2);
		this.proxyFactory.setHessian2Reply(hessian2);
	}

	/**
	 * Set whether version 2 of the Hessian protocol should be used for parsing
	 * requests. Default is "false".
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setHessian2Request
	 */
	public void setHessian2Request(boolean hessian2) {
		this.proxyFactory.setHessian2Request(hessian2);
	}

	/**
	 * Set whether version 2 of the Hessian protocol should be used for parsing
	 * replies. Default is "false".
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setHessian2Reply
	 */
	public void setHessian2Reply(boolean hessian2) {
		this.proxyFactory.setHessian2Reply(hessian2);
	}

	/**
	 * Convert the given Hessian access exception to an appropriate Spring
	 * RemoteAccessException.
	 * 
	 * @param ex
	 *            the exception to convert
	 * @return the RemoteAccessException to throw
	 */
	protected RemoteAccessException convertHessianAccessException(Throwable ex,
			InvokeTagetInfo target) {
		if (ex instanceof ConnectException) {
			return new RemoteConnectFailureException(
					"Cannot connect to Hessian remote service at ["
							+ target.getUrl() + "]", ex);
		} else {
			return new RemoteAccessException(
					"Cannot access Hessian remote service at ["
							+ target.getUrl() + "]", ex);
		}
	}
}
