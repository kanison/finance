/**
 * 
 */
package com.tenpay.sm.lang.health.spring;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


import com.tenpay.sm.lang.health.Health;

/**
 * @author torryhong
 *
 */
public class ThreadPoolTaskHealth implements Health ,ApplicationContextAware {
	private ApplicationContext applicationContext;
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.lang.health.Health#check()
	 */
	public Object check() {
		Map map = new TreeMap();
		String[] beanNames = this.applicationContext.getBeanNamesForType(ThreadPoolTaskExecutor.class, false, false);
		if(beanNames!=null) {
			for(String beanName : beanNames) {
				ThreadPoolTaskExecutor tpte = (ThreadPoolTaskExecutor)this.applicationContext.getBean(beanName, ThreadPoolTaskExecutor.class);
				Map status = new TreeMap();
				status.put("activeCount", tpte.getActiveCount());
				status.put("corePoolSize", tpte.getCorePoolSize());
				status.put("maxPoolSize", tpte.getMaxPoolSize());
				status.put("poolSize", tpte.getPoolSize());
				status.put("executor.queue.size", tpte.getThreadPoolExecutor().getQueue().size());
				status.put("executor.taskCount", tpte.getThreadPoolExecutor().getTaskCount());
				status.put("executor.queue.remainingCapacity", tpte.getThreadPoolExecutor().getQueue().remainingCapacity());
				map.put(beanName.replace("#", ".sharp."), status);
			}
		}
		return map;
	}

}
