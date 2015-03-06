/**
 * 
 */
package com.tenpay.sm.lang.health.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.tenpay.sm.lang.health.Health;

/**
 * @author torryhong
 * 
 */
public class ApplicationContextHealth implements Health,
		ApplicationContextAware {
	private ApplicationContext applicationContext;
	private final static String id = String.valueOf(System.currentTimeMillis());

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tenpay.sm.lang.health.Health#check()
	 */
	public Object check() {
		ApplicationContextStatus status = new ApplicationContextStatus();
		status.setBeanCount(this.applicationContext.getBeanDefinitionCount());
		// status.setBeanNames(this.applicationContext.getBeanDefinitionNames());
		status.setId(id);
		status.setStartUpDate(new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS").format(new java.util.Date(
				this.applicationContext.getStartupDate())));
		status.setTimestamp(System.currentTimeMillis());
		return status;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
