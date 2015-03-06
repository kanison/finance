/**
 * 
 */
package com.tenpay.sm.lang.async;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

/**
 * �첽ִ�з����ķ���������,�����쳣
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
			// �߳�����ʧ��Ҳ���׳��쳣
			logger.warn("�첽�����߳��쳣:", t);
			return null;
		}
	}

}
