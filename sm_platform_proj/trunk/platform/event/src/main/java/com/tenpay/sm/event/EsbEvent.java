/**
 * 
 */
package com.tenpay.sm.event;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Administrator
 * ESBÊÂ¼þ
 */
public class EsbEvent implements Serializable {
	private String eventType;
	private String eventId;
	private String eventData;
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);	
	}
	
	/**
	 * @return the eventData
	 */
	public String getEventData() {
		return eventData;
	}
	/**
	 * @param eventData the eventData to set
	 */
	public void setEventData(String eventData) {
		this.eventData = eventData;
	}
	/**
	 * @return the eventId
	 */
	public String getEventId() {
		return eventId;
	}
	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	/**
	 * @return the eventType
	 */
	public String getEventType() {
		return eventType;
	}
	/**
	 * @param eventType the eventType to set
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
}
