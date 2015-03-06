/**
 * 
 */
package com.tenpay.sm.web.session;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * @author li.hongtl
 *
 */
public class HttpSessionModel implements Serializable {
	private static final long serialVersionUID = -7832477718811756324L;
	public HttpSessionModel() {
		id = java.util.UUID.randomUUID().toString();
	}
	private final long creationTime = System.currentTimeMillis();
	private String id;
	private final Hashtable<String ,java.io.Serializable> attributes = new Hashtable<String,java.io.Serializable>();
	private long lastAccessedTime = System.currentTimeMillis();
	private int maxInactiveInterval=30*60;
	/**
	 * @return the attributes
	 */
	public Hashtable<String, java.io.Serializable> getAttributes() {
		return attributes;
	}

	/**
	 * @return the creationTime
	 */
	public long getCreationTime() {
		return creationTime;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the lastAccessedTime
	 */
	public long getLastAccessedTime() {
		return lastAccessedTime;
	}

	/**
	 * @param lastAccessedTime the lastAccessedTime to set
	 */
	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}
	
	public void invalidate() {
		this.attributes.clear();
		this.maxInactiveInterval=30*60;
		this.lastAccessedTime = System.currentTimeMillis();
	}

	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}
	
}
