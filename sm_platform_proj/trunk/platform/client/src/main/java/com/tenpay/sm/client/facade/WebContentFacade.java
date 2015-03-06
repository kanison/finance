/**
 * 
 */
package com.tenpay.sm.client.facade;


/**
 * 根据path获得http GET请求的web 内容
 * 不包含重定向
 * @author li.hongtl
 *
 */
public interface WebContentFacade {
	/**
	 * 根据path获得http GET请求的web 内容
	 * 不包含重定向
	 * @param path
	 * @param request
	 * @return
	 */
	String httpGetWebContent(String path,HttpRequestModel request);
}
