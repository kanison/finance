/**
 * 
 */
package com.tenpay.sm.event.config;

import java.io.Serializable;

import javax.jms.Session;

/**
 * @author Administrator
 *
 */
public class JmsSessionConfig implements Serializable {
	private JmsConnectionConfig connectionConfig;
	private boolean transactioned = false;
	private int acknowledge = Session.AUTO_ACKNOWLEDGE;
	
	
	/**
	 * @return the connectionConfig
	 */
	public JmsConnectionConfig getConnectionConfig() {
		return connectionConfig;
	}
	/**
	 * @param connectionConfig the connectionConfig to set
	 */
	public void setConnectionConfig(JmsConnectionConfig connectionConfig) {
		this.connectionConfig = connectionConfig;
	}
	/**
	 * @return the acknowledge
	 */
	public int getAcknowledge() {
		return acknowledge;
	}
	/**
	 * @param acknowledge the acknowledge to set
	 */
	public void setAcknowledge(int acknowledge) {
		this.acknowledge = acknowledge;
	}
	/**
	 * @return the transactioned
	 */
	public boolean isTransactioned() {
		return transactioned;
	}
	/**
	 * @param transactioned the transactioned to set
	 */
	public void setTransactioned(boolean transactioned) {
		this.transactioned = transactioned;
	}
	
	
}
