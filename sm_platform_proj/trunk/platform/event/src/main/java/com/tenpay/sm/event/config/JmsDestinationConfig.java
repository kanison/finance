/**
 * 
 */
package com.tenpay.sm.event.config;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class JmsDestinationConfig implements Serializable {
	private JmsSessionConfig sessionConfig;
    private String subject;
    private boolean topic = true;

    
    
    
	/**
	 * @return the sessionConfig
	 */
	public JmsSessionConfig getSessionConfig() {
		return sessionConfig;
	}
	/**
	 * @param sessionConfig the sessionConfig to set
	 */
	public void setSessionConfig(JmsSessionConfig sessionConfig) {
		this.sessionConfig = sessionConfig;
	}
	
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the topic
	 */
	public boolean isTopic() {
		return topic;
	}
	/**
	 * @param topic the topic to set
	 */
	public void setTopic(boolean topic) {
		this.topic = topic;
	}
    
    
}
