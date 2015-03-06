/**
 * 
 */
package com.tenpay.sm.client.soagov;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.transport.http.*;

import com.tenpay.sm.common.lang.diagnostic.Profiler;
/**
 * @author torryhong
 *
 */
public class RequestHeaderXFireSoaGovInterceptor implements MethodInterceptor {
	public static final String KEY_SOAGOV_INVOKE_GLOBALID = "SOAGOV.INVOKE.GLOBALID";
	public static final String KEY_SOAGOV_INVOKE_RESPONSEHEADER = "SOAGOV.INVOKE.RESPONSEHEADER";
	private static ThreadLocal<String> invokeGlobalId = new ThreadLocal<String>();
	private static ThreadLocal<Integer> invokeIndex = new ThreadLocal<Integer>();
	private static ThreadLocal<String> invokeSoaGovResponseHeader = new ThreadLocal<String>();
	
	public static void setInvokeSoaGovResponseHeader(String content) {
		invokeSoaGovResponseHeader.set(content);
	}
	public static String getInvokeSoaGovResponseHeader() {
		return invokeSoaGovResponseHeader.get();
	}
	
	public static void reset(String parentId) {
		invokeGlobalId.set(parentId);
		invokeIndex.set(null);
		invokeSoaGovResponseHeader.set(null);
	}
	
	public static String getInvokeGlobalId() {
		String id = invokeGlobalId.get();
		if(id==null || "".equals(id)) {
			try {
				id = java.net.InetAddress.getLocalHost().getHostName() + "-" + UUID.randomUUID().toString();
			} catch (java.net.UnknownHostException e) {
				id = "UnknowHost" + "-" + UUID.randomUUID().toString();
			}
			invokeGlobalId.set(id);
		}
		return invokeGlobalId.get();
	}
	
	/* (non-Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object service = invocation.getThis();
		Client client = Client.getInstance(service);
		
		client.setProperty(AbstractMessageSender.MESSAGE_SENDER_CLASS_NAME, SoaGovCommonsHttpMessageSender.class.getName());
		
		Map headersMap = (Map)client.getProperty(CommonsHttpMessageSender.HTTP_HEADERS);
		if(headersMap==null) {
			headersMap = (Map)client.getService().getProperty(CommonsHttpMessageSender.HTTP_HEADERS);
		}
		if(headersMap==null) {
			headersMap = new HashMap();
			client.setProperty(CommonsHttpMessageSender.HTTP_HEADERS,headersMap);
		}
		
		Integer index = invokeIndex.get();
		if(index==null) {
			index = new Integer(1);
		} else {
			index = index.intValue() + 1;
		}
		invokeIndex.set(index);
		
		String id = getInvokeGlobalId();
		headersMap.put(KEY_SOAGOV_INVOKE_GLOBALID, id + "." + index);
		
		try {
			Object returnValue = invocation.proceed();
			return returnValue;
		} finally {
			Profiler.enter("<a href='#'  onclick='select_changed(this)'>远程调用返回的soa头:</a><div style='display:none'>" 
					+ getInvokeSoaGovResponseHeader() + "</div>");
			Profiler.release();
		}
	}

}
