/**
 * 
 */
package com.tenpay.sm.event.asyncinvoke;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.tenpay.sm.event.EsbEvent;
import com.tenpay.sm.event.listener.EsbEventListener;
import com.tenpay.sm.lang.util.Base64;
import com.tenpay.sm.lang.util.MethodUtil;

/**
 * @author torryhong
 *
 */
public class MethodInvokeEsbEventListener implements EsbEventListener , BeanFactoryAware {
	private static Logger logger = Logger.getLogger(MethodInvokeEsbEventListener.class);
	private BeanFactory beanFactory;
	private Map<String,Object> beans;
	private boolean invokeBeansFromBeanFactory = true;
	
	/* (non-Javadoc)
	 * @see com.tenpay.sm.event.listener.EsbEventListener#onEvent(com.tenpay.sm.event.EsbEvent)
	 */
	public void onEvent(EsbEvent esbEvent) {
		if(esbEvent!=null && esbEvent.getEventData()!=null) {
			MethodInvokeDescriptor methodInvokeDescriptor = null;
			try {
				java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(
						Base64.fromBase64Bytes(esbEvent.getEventData()));
				java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bais);
				methodInvokeDescriptor = (MethodInvokeDescriptor)ois.readObject();
			} catch (Exception e) {
				logger.warn("收到esbEvent，methodInvokeDescriptor数据有误：" + esbEvent,e);
				return;
			} 
			if(methodInvokeDescriptor!=null && StringUtils.isNotBlank(methodInvokeDescriptor.getBeanName()) 
					&& StringUtils.isNotBlank(methodInvokeDescriptor.getMethodName())) {
				Object bean = null;
				if(this.beans!=null) {
					bean = this.beans.get(methodInvokeDescriptor.getBeanName());
				}
				if(bean==null && this.invokeBeansFromBeanFactory) {
					bean = this.beanFactory.getBean(methodInvokeDescriptor.getBeanName());
				} 
				if(bean!=null) {
					MethodUtil.invokeNested(bean, methodInvokeDescriptor.getMethodName(),
							methodInvokeDescriptor.getTypes(), methodInvokeDescriptor.getArguments());
				} else {
					logger.warn("收到esbEvent，找不到bean：" + esbEvent);
					return;
				}
			}
			else {
				logger.warn("收到esbEvent，methodInvokeDescriptor数据有误：" + esbEvent);
				return;
			}
		} else {
			logger.warn("收到esbEvent，数据有误：" + esbEvent);
			return;
		}		
	}
	
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	/**
	 * @return the beans
	 */
	public Map<String, Object> getBeans() {
		return beans;
	}
	/**
	 * @param beans the beans to set
	 */
	public void setBeans(Map<String, Object> beans) {
		this.beans = beans;
	}
	/**
	 * @return the invokeBeansFromBeanFactory
	 */
	public boolean isInvokeBeansFromBeanFactory() {
		return invokeBeansFromBeanFactory;
	}
	/**
	 * @param invokeBeansFromBeanFactory the invokeBeansFromBeanFactory to set
	 */
	public void setInvokeBeansFromBeanFactory(boolean invokeBeansFromBeanFactory) {
		this.invokeBeansFromBeanFactory = invokeBeansFromBeanFactory;
	}
	
	
}
