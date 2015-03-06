/**
 * 
 */
package com.tenpay.sm.lang.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author torryhong
 *
 */
public interface SpringQuartzJobBean {
	abstract void executeSchedule(JobExecutionContext context) throws JobExecutionException;
}
