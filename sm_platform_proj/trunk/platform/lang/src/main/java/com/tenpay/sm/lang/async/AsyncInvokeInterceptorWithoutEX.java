/**
 * 
 */
package com.tenpay.sm.lang.async;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

/**
 * 异步执行方法的方法拦截器,不抛异常
 * 
 * @author eniacli
 */
public class AsyncInvokeInterceptorWithoutEX extends AsyncInvokeInterceptor {
	private static final Logger logger = Logger
			.getLogger(AsyncInvokeInterceptorWithoutEX.class);

	public Object invoke(final MethodInvocation invocation) throws Throwable {
		try {
			return super.invoke(invocation);
		} catch (Throwable t) {
			// 线程启动失败也不抛出异常
			logger.warn("异步启动线程异常:", t);
			return null;
		}
	}

}
