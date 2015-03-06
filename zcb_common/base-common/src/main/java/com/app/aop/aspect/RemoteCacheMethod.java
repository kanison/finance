package com.app.aop.aspect;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

import com.app.utils.AliSpyMemCachedWrapper;
import com.app.utils.CachedValueContainer;
import com.app.utils.MD5Util;

public class RemoteCacheMethod implements CacheMethodFacade {
	private static final Log LOG = LogFactory.getLog(RemoteCacheMethod.class);
	private long defaultCacheMs = 600 * 1000;
	private AliSpyMemCachedWrapper aliSpyMemCache;

	public RemoteCacheMethod() {
		
	}

	private Object getRemoteCache(ProceedingJoinPoint joinPoint, String key, int expire)
			throws Throwable {
		Object result=null;
		try {
			result = aliSpyMemCache.get(key);
		} catch (Exception e) {
			//异常当没取到，降级继续查询数据库
			LOG.info("RemoteCacheMethod getCache exception.key=" + key);
		}
		
		if (result == null) {
			try {
				result = joinPoint.proceed();
			} catch (Throwable t) {
				LOG.warn("getRemoteCache ex for " + key, t);
				throw (t);
			}
			// 特殊处理，不缓存null
			if (result == null) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("remoteCache返回数据无效:" + key);
				}
				return null;
			}
			aliSpyMemCache.put(key, expire, result);
			if (LOG.isDebugEnabled()) {
				LOG.debug("remoteCache缓存未命中:" + key);
			}
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("remoteCache缓存命中:" + key);
			}
		}
		return result;
	}

	public Object cacheMethod(ProceedingJoinPoint joinPoint) throws Throwable {

		return cacheMethod(joinPoint, defaultCacheMs);
	}

	public Object cacheMethod(ProceedingJoinPoint joinPoint, long cacheMs)
			throws Throwable {
		return cacheMethod(joinPoint, cacheMs, 0);
	}

	public Object cacheMethod(ProceedingJoinPoint joinPoint, long cacheMs,
			int classOrObj) throws Throwable {
		//LOG.info("use remoteCacheMethod.");
		Object targetObj = joinPoint.getTarget();
		String className = targetObj.getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		StringBuffer sb = new StringBuffer();
		sb.append(className);
		sb.append('@');
		if (classOrObj == 1) {
			// 按对象缓存
			sb.append(targetObj.hashCode());
			sb.append(".");
		}
		sb.append(methodName);
		sb.append(":");
		Object args[] = joinPoint.getArgs();
		for (int i = 0; i < args.length; i++) {
			Object obj = args[i];
			if(obj == null ){
				//LOG.info("0 obj=[" + obj +"]");
				continue;//null不参与key计算
			}
			else if ((obj instanceof CharSequence) || (obj instanceof Number)
					|| (obj instanceof Character) || (obj instanceof Boolean)
					|| (obj instanceof Date)) {
				//LOG.info("1 obj=[" + obj +"]");
				sb.append(obj.toString());
			} else {
				//LOG.info("2 obj=[" + obj +"]");
				sb.append(ToStringBuilder.reflectionToString(obj,
						ToStringStyle.SIMPLE_STYLE));
			}
			sb.append(";");
		}
		String key = MD5Util.getMD5(sb.toString(), "GBK");
		/*
		// 保护行为，防止key过大导致内存溢出
		if (sb.length() > 8192)
			key = sb.substring(0, 8192);
		else
			key = sb.toString();
		*/
		// 取缓存
		return getRemoteCache(joinPoint, key, (int)(cacheMs/1000));	
	}



	public void cleanCacheValue() {
		
	}

	public Object updateCache(ProceedingJoinPoint joinPoint, long cacheMs,
			String key, CachedValueContainer cachedValueContainer)
			throws Throwable {
		return null;
	}

	

	public void setAliSpyMemCache(AliSpyMemCachedWrapper aliSpyMemCache) {
		this.aliSpyMemCache = aliSpyMemCache;
	}

	public AliSpyMemCachedWrapper getAliSpyMemCache() {
		return aliSpyMemCache;
	}
}
