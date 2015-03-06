/**
 * 
 */
package com.tenpay.sm.event;

/**
 * @author torryhong
 * �¼���������
 */
public interface EsbEventEngine {
	/**
	 * 
	 * @param object
	 * @param channel
	 * @param ignoreException
	 */
	public void publicObject(Object object, final String channel,boolean ignoreException);
	
	/**
	 * 
	 * @param event
	 * @param channel
	 * @param ignoreException
	 */
	public void publicEsbEvent(EsbEvent event,String channel,boolean ignoreException);
}
