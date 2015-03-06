/**
 * 
 */
package com.tenpay.sm.lang.health.spring;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.listener.AbstractMessageListenerContainer;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.serversession.ServerSessionMessageListenerContainer;

import com.tenpay.sm.lang.health.Health;

/**
 * @author torryhong
 *
 */
public class MessageListenerContainerHealth implements Health ,ApplicationContextAware {
	private ApplicationContext applicationContext;
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	/* (non-Javadoc)
	 * @see com.tenpay.sm.lang.health.Health#check()
	 */
	public Object check() {
		Map map = new TreeMap();
		String[] beanNames = this.applicationContext.getBeanNamesForType(AbstractMessageListenerContainer.class, false, false);
		if(beanNames!=null) {
			for(String beanName : beanNames) {
				AbstractMessageListenerContainer mlc = (AbstractMessageListenerContainer)this.applicationContext.getBean(beanName, AbstractMessageListenerContainer.class);
				Map status = new TreeMap();
				status.put("clientId", mlc.getClientId());
				status.put("destinationName", mlc.getDestinationName());
				status.put("pausedTaskCount", mlc.getPausedTaskCount());
				status.put("sessionAcknowledgeMode", mlc.getSessionAcknowledgeMode());
				
				if(mlc instanceof DefaultMessageListenerContainer) {
					DefaultMessageListenerContainer dmlc = (DefaultMessageListenerContainer)mlc;
					status.put("activeConsumerCount", dmlc.getActiveConsumerCount());
					status.put("cacheLevel", dmlc.getCacheLevel());
					status.put("concurrentConsumers", dmlc.getConcurrentConsumers());
					status.put("pausedTaskCount", dmlc.getPausedTaskCount());
					status.put("scheduledConsumerCount", dmlc.getScheduledConsumerCount());
				}
				
				if(mlc instanceof ServerSessionMessageListenerContainer) {
					ServerSessionMessageListenerContainer ssmlc = (ServerSessionMessageListenerContainer)mlc;
					status.put("pausedTaskCount", ssmlc.getPausedTaskCount());
				}
				map.put(beanName.replace("#", ".sharp."), status);
			}
		}
		return map;
	}
}
