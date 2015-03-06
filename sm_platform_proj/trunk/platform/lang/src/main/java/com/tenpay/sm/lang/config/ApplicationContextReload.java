/**
 * 
 */
package com.tenpay.sm.lang.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.tenpay.sm.lang.schedule.SpringQuartzJobBean;

/**
 * @author torryhong
 *
 */
public class ApplicationContextReload implements ApplicationContextAware, SpringQuartzJobBean {
	private static final Log LOG = LogFactory.getLog(ApplicationContextReload.class);
	public static final String spring_timestamp_key = "spring_timestamp"; 
	AppConfig appConfig;
	ConfigurableApplicationContext configurableApplicationContext;
	String spring_timestamp_value;
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if(applicationContext!=null && applicationContext instanceof ConfigurableApplicationContext) {
			this.configurableApplicationContext = (ConfigurableApplicationContext)applicationContext;
		}
		
	}

	/**
	 * @param configurableApplicationContext the configurableApplicationContext to set
	 */
	public void setConfigurableApplicationContext(
			ConfigurableApplicationContext configurableApplicationContext) {
		this.configurableApplicationContext = configurableApplicationContext;
	}


	/**
	 * @param appConfig the appConfig to set
	 */
	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
		this.spring_timestamp_value = this.appConfig.get(spring_timestamp_key);
	}

	synchronized public void executeSchedule(JobExecutionContext context) throws JobExecutionException {
		String timestamp_value = this.appConfig.get(spring_timestamp_key);
		if(timestamp_value!=null && this.spring_timestamp_value!=null &&
				!timestamp_value.equals(spring_timestamp_value)) {
			try {
				if(LOG.isWarnEnabled()) {
					LOG.warn("开始刷新Spring上下文！");
				}
				this.configurableApplicationContext.refresh();
				if(LOG.isWarnEnabled()) {
					LOG.warn("刷新Spring上下文成功！");
				}
				this.spring_timestamp_value = timestamp_value;
			} catch(BeansException ex) {
				LOG.fatal("严重问题：Spring上下文加载失败！，可能需要立即重启应用！",ex);
			}
		}
			
	}
	
	

}
