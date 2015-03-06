/**
 * 
 */
package com.tenpay.sm.lang.health.spring;

import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.tenpay.sm.lang.health.Health;

/**
 * @author torryhong
 *
 */
public class QuartzHealth implements Health ,ApplicationContextAware {
	private static final Logger logger = Logger.getLogger(QuartzHealth.class);
	private ApplicationContext applicationContext;
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.lang.health.Health#check()
	 */
	public Object check() {
		Map map = new TreeMap();
		//TODO Scheduler or SchedulerFactoryBean ?
		String[] beanNames = this.applicationContext.getBeanNamesForType(Scheduler.class, false, false);
		if(beanNames!=null) {
			for(String beanName : beanNames) {
				Scheduler scheduler = (Scheduler)this.applicationContext.getBean(beanName, Scheduler.class);
				Map status = new TreeMap();
				//什么数据，可能危险！FIXME
				try {
					status.put("threads", "TODO thread counts");
					status.put("currentlyExecutingJobs", scheduler.getCurrentlyExecutingJobs());
					status.put("globalJobListeners", scheduler.getGlobalJobListeners());
					status.put("globalTriggerListeners", scheduler.getGlobalTriggerListeners());
				} catch (SchedulerException e) {
					logger.error("SchedulerException get detail error",e);
				}
				map.put(beanName.replace("#", ".sharp."), status);
			}
		}
		return map;
	}
}
