/**
 * 
 */
package com.tenpay.sm.event.asyncinvoke;

import java.util.UUID;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.ClassUtils;

import com.tenpay.sm.event.EsbEvent;
import com.tenpay.sm.event.EsbEventEngine;
import com.tenpay.sm.lang.util.Base64;

/**
 * @author Administrator
 * 事件发布引擎
 */
public class EsbAsyncInvokeInterceptor implements MethodInterceptor {
	private EsbEventEngine asyncInvokeEsbEventEngine;
	private String channel;
	private boolean ignoreException = false;
	private String targetBeanName;
	/* (non-Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		MethodInvokeDescriptor methodInvokeDescriptor = new MethodInvokeDescriptor();
		if(targetBeanName==null) {
			targetBeanName = ClassUtils.getShortNameAsProperty(methodInvocation.getThis().getClass());
		}
		methodInvokeDescriptor.setBeanName(targetBeanName);
		methodInvokeDescriptor.setMethodName(methodInvocation.getMethod().getName());
		methodInvokeDescriptor.setTypes(methodInvocation.getMethod().getParameterTypes());
		methodInvokeDescriptor.setArguments(methodInvocation.getArguments());
		
		EsbEvent esbEvent = new EsbEvent();
		esbEvent.setEventType("EsbAsyncInvoke." + methodInvokeDescriptor.getBeanName() + "." + methodInvokeDescriptor.getMethodName());
		esbEvent.setEventId(esbEvent.getEventType() + "." + UUID.randomUUID().toString());
		
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
		oos.writeObject(methodInvokeDescriptor);
		String eventData = Base64.toBase64(baos.toByteArray());
		esbEvent.setEventData(eventData);
		
		asyncInvokeEsbEventEngine.publicObject(esbEvent, channel, ignoreException);
		//WARN，不可能得到返回值
		return null;
	}
	/**
	 * @return the asyncInvokeEsbEventEngine
	 */
	public EsbEventEngine getAsyncInvokeEsbEventEngine() {
		return asyncInvokeEsbEventEngine;
	}
	/**
	 * @param asyncInvokeEsbEventEngine the asyncInvokeEsbEventEngine to set
	 */
	public void setAsyncInvokeEsbEventEngine(
			EsbEventEngine asyncInvokeEsbEventEngine) {
		this.asyncInvokeEsbEventEngine = asyncInvokeEsbEventEngine;
	}
	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}
	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	/**
	 * @return the ignoreException
	 */
	public boolean isIgnoreException() {
		return ignoreException;
	}
	/**
	 * @param ignoreException the ignoreException to set
	 */
	public void setIgnoreException(boolean ignoreException) {
		this.ignoreException = ignoreException;
	}
	/**
	 * @return the targetBeanName
	 */
	public String getTargetBeanName() {
		return targetBeanName;
	}
	/**
	 * @param targetBeanName the targetBeanName to set
	 */
	public void setTargetBeanName(String targetBeanName) {
		this.targetBeanName = targetBeanName;
	}


}
