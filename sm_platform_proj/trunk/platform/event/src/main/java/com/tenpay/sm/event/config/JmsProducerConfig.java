/**
 * 
 */
package com.tenpay.sm.event.config;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class JmsProducerConfig implements Serializable {
	private JmsDestinationConfig destinationConfig;
	
    private boolean persistent = false;
    private long timeToLive = 0;
    private int priority = -1;
	/**
	 * @return the persistent
	 */
	public boolean isPersistent() {
		return persistent;
	}
	/**
	 * @param persistent the persistent to set
	 */
	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}
	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * @return the timeToLive
	 */
	public long getTimeToLive() {
		return timeToLive;
	}
	/**
	 * @param timeToLive the timeToLive to set
	 */
	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}
	/**
	 * @return the destinationConfig
	 */
	public JmsDestinationConfig getDestinationConfig() {
		return destinationConfig;
	}
	/**
	 * @param destinationConfig the destinationConfig to set
	 */
	public void setDestinationConfig(JmsDestinationConfig destinationConfig) {
		this.destinationConfig = destinationConfig;
	}

	
    
}
