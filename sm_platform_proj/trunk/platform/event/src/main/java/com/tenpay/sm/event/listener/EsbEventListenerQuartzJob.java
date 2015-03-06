/**
 * 
 */
package com.tenpay.sm.event.listener;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.tenpay.sm.event.EsbEvent;

/**
 * @author Administrator
 *
 */
@Deprecated
public class EsbEventListenerQuartzJob extends QuartzJobBean {
	private static Logger logger = Logger.getLogger(EsbEventListenerQuartzJob.class);
	
	private static Long[] lastSuccessMessageTimestamps = new Long[1001];
	private static Object[] objectForSynchronized = new Object[1001];
	static {
		for(int i=0;i<lastSuccessMessageTimestamps.length;i++) {
			lastSuccessMessageTimestamps[i] = 0L;
			objectForSynchronized[i] = new Object();
		}
	}
	
	private EsbEventListener esbEventListener;
	private JmsTemplate defaultJmsTemplate = null;
	
	private int jobIndex = 0;
	
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		if(logger.isDebugEnabled()) {
			logger.debug(new java.util.Date() + "任务执行: " + context.getJobDetail().getJobClass());
		}
		synchronized(objectForSynchronized[jobIndex]) {
			Message message = null;
			try {
				message = defaultJmsTemplate.receive();
				if(message==null) {
					if(logger.isDebugEnabled()) {
						logger.debug(jobIndex + "," + new java.util.Date() + "没有收到消息: " + context.getJobDetail().getJobClass());
					}
					return;
				}
				if(logger.isInfoEnabled()) {
					logger.info(jobIndex + "," + message.getJMSMessageID() + ",lsmts:" + lastSuccessMessageTimestamps[jobIndex] + " 收到消息：" + "----" + message);
				}
				if(lastSuccessMessageTimestamps[jobIndex].longValue() > message.getJMSTimestamp()) {
					if(logger.isInfoEnabled()) {
						logger.info(jobIndex + ",忽略重复的消息,id:" + message.getJMSMessageID());
					}
					return;
				}
			} catch (JmsException e) {
				logger.error(jobIndex + ",接收消息错误: " + e.getMessage(),e);
				return;
			} catch (Throwable e) {
				logger.error(jobIndex + ",接收消息错误: " + e.getMessage(),e);
				return;
			} 
			
			EsbEvent esbEvent;
			try {
				MapMessage mapMsg = (MapMessage)message;
				esbEvent = new EsbEvent();
				esbEvent.setEventId(mapMsg.getString("eventId"));
				esbEvent.setEventType(mapMsg.getString("eventType"));
				esbEvent.setEventData(mapMsg.getString("eventData"));
			} catch (JMSException e) {
				logger.error(jobIndex + ",接收内容格式可能有错: " + e.getMessage(),e);
				return;
			} catch (Throwable e) {
				logger.error(jobIndex + ",接收内容格式可能有错: " + e.getMessage(),e);
				return;
			}
			
			try {
				if(logger.isInfoEnabled()) {
					logger.info(jobIndex + ",处理消息,id:" + message.getJMSMessageID());
				}
				esbEventListener.onEvent(esbEvent);
				lastSuccessMessageTimestamps[jobIndex] = message.getJMSTimestamp();
				message.acknowledge();
			} catch (Throwable e) {
				logger.error(jobIndex + ",处理消息出错: " + e.getMessage(),e);
				return;
			}
		}
	}

	/**
	 * @return the defaultJmsTemplate
	 */
	public JmsTemplate getDefaultJmsTemplate() {
		return defaultJmsTemplate;
	}

	/**
	 * @param defaultJmsTemplate the defaultJmsTemplate to set
	 */
	public void setDefaultJmsTemplate(JmsTemplate defaultJmsTemplate) {
		this.defaultJmsTemplate = defaultJmsTemplate;
	}


	/**
	 * @return the esbEventListener
	 */
	public EsbEventListener getEsbEventListener() {
		return esbEventListener;
	}


	/**
	 * @param esbEventListener the esbEventListener to set
	 */
	public void setEsbEventListener(EsbEventListener esbEventListener) {
		this.esbEventListener = esbEventListener;
	}

	/**
	 * @return the jobIndex
	 */
	public int getJobIndex() {
		return jobIndex;
	}

	/**
	 * @param jobIndex the jobIndex to set
	 */
	public void setJobIndex(int jobIndex) {
		this.jobIndex = jobIndex;
	}
	
	
}
