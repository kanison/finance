/**
 * 
 */
package com.tenpay.sm.client.facade;


/**
 * ����path���http GET�����web ����
 * �������ض���
 * @author li.hongtl
 *
 */
public interface WebContentFacade {
	/**
	 * ����path���http GET�����web ����
	 * �������ض���
	 * @param path
	 * @param request
	 * @return
	 */
	String httpGetWebContent(String path,HttpRequestModel request);
}
