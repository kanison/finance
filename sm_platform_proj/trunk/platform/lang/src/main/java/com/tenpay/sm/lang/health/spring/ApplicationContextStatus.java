/**
 * 
 */
package com.tenpay.sm.lang.health.spring;

/**
 * @author torryhong
 * 
 */
public class ApplicationContextStatus {
	private int beanCount;
	private String[] beanNames;
	private String startUpDate;
	private String id;
	private long timestamp;

	/**
	 * @return the beanCount
	 */
	public int getBeanCount() {
		return beanCount;
	}

	/**
	 * @param beanCount
	 *            the beanCount to set
	 */
	public void setBeanCount(int beanCount) {
		this.beanCount = beanCount;
	}

	/**
	 * @return the beanNames
	 */
	public String[] getBeanNames() {
		return beanNames;
	}

	/**
	 * @param beanNames
	 *            the beanNames to set
	 */
	public void setBeanNames(String[] beanNames) {
		this.beanNames = beanNames;
	}

	/**
	 * @return the startUpDate
	 */
	public String getStartUpDate() {
		return startUpDate;
	}

	/**
	 * @param startUpDate
	 *            the startUpDate to set
	 */
	public void setStartUpDate(String startUpDate) {
		this.startUpDate = startUpDate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
