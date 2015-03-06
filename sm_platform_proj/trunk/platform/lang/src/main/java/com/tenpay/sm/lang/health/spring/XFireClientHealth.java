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
public class XFireClientHealth implements Health,ApplicationContextAware {
	private ApplicationContext applicationContext;
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.lang.health.Health#check()
	 */
	public Object check() {
		// TODO Auto-generated method stub
		return null;
	}

}
