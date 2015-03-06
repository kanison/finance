/**
 * 
 */
package com.tenpay.sm.event.engine;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.tenpay.sm.event.EsbEvent;
import com.tenpay.sm.event.EsbEventEngine;
import com.tenpay.sm.event.convert.EsbEventMessageConverter;

/**
 * @author torryhong
 * �¼���������
 */
public class EsbEventEngineImpl implements EsbEventEngine {
	private static Logger logger = Logger.getLogger(EsbEventEngineImpl.class);
	
	private Map<String,JmsTemplate> jmsTemplates;
	private JmsTemplate defaultJmsTemplate = null;
	private MessageConverter messageConverter = new EsbEventMessageConverter();
	private boolean afterTransactionCommit = true;
	
	public void publicEsbEvent(final EsbEvent event,String channel,boolean ignoreException) {
		publicObject(event,channel,ignoreException);
	}
	/* (non-Javadoc)
	 * @see com.tenpay.sm.event.EsbEventEngine#publicESBEvent(com.tenpay.sm.event.EsbEvent, com.tenpay.sm.event.ESBChannel)
	 */
	public void publicObject(final Object object, final String channel,final boolean ignoreException) {
		 if (afterTransactionCommit && TransactionSynchronizationManager.isActualTransactionActive()) {
		    if (logger.isInfoEnabled()) {
				logger.info("���ڴ��������У��ݻ������첽ִ�У�ע������ͬ������");
			}
			TransactionSynchronizationManager
					.registerSynchronization(new TransactionSynchronizationAdapter() {
						public void afterCommit() {
							EsbEventEngineImpl.this.publicObjectInternal(object, channel, ignoreException);
						}
					});
		} 
		else {
			EsbEventEngineImpl.this.publicObjectInternal(object,channel,ignoreException);
		}				
	}
	/* (non-Javadoc)
	 * @see com.tenpay.sm.event.EsbEventEngine#publicESBEvent(com.tenpay.sm.event.EsbEvent, com.tenpay.sm.event.ESBChannel)
	 */
	public void publicObjectInternal(final Object object, final String channel,boolean ignoreException) {
		try {
			JmsTemplate jmsTemplate = null;
			if(jmsTemplates!=null) {
				jmsTemplate = jmsTemplates.get(channel);
			}
			if(jmsTemplate==null) {
				jmsTemplate = defaultJmsTemplate;
			}
			
			if(jmsTemplate!=null) {
				jmsTemplate.send(new MessageCreator() {
					public Message createMessage(Session session) throws JMSException {
						Message message = messageConverter.toMessage(object, session);
						return message;
					}
				});
			}
			else {
				logger.error("û��jmsTemplate��"+channel+"���ܷ�����Ϣ�¼�:" + object);
				if(!ignoreException) {
					throw new RuntimeException("û��jmsTemplate��"+channel+"���ܷ�����Ϣ�¼�:" + object);
				}
			}
		} catch (RuntimeException e) {
			logger.error("����esb�¼�����:" + object,e);
			if(!ignoreException) {
				throw e;
			}
		}
	}

	/**
	 * @return the jmsTemplates
	 */
	public Map<String, JmsTemplate> getJmsTemplates() {
		return jmsTemplates;
	}

	/**
	 * @param jmsTemplates the jmsTemplates to set
	 */
	public void setJmsTemplates(Map<String, JmsTemplate> jmsTemplates) {
		this.jmsTemplates = jmsTemplates;
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
	 * @return the messageConverter
	 */
	public MessageConverter getMessageConverter() {
		return messageConverter;
	}

	/**
	 * @param messageConverter the messageConverter to set
	 */
	public void setMessageConverter(MessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}
	
	public boolean isAfterTransactionCommit() {
		return afterTransactionCommit;
	}
	public void setAfterTransactionCommit(boolean afterTransactionCommit) {
		this.afterTransactionCommit = afterTransactionCommit;
	}



}
