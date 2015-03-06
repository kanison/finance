/**
 * 
 */
package com.tenpay.sm.lang.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author torryhong
 *
 */
public class SpringQuartzJobBeanBridge extends QuartzJobBean {
	private SpringQuartzJobBean springQuartzJobBean;

	/**
	 * @return the springQuartzJobBean
	 */
	public SpringQuartzJobBean getSpringQuartzJobBean() {
		return springQuartzJobBean;
	}

	/**
	 * @param springQuartzJobBean the springQuartzJobBean to set
	 */
	public void setSpringQuartzJobBean(SpringQuartzJobBean springQuartzJobBean) {
		this.springQuartzJobBean = springQuartzJobBean;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		this.springQuartzJobBean.executeSchedule(context);
	}
}
