/**
 * 
 */
package com.tenpay.sm.client.facade;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;

/**
 * @author li.hongtl
 *
 */
public class HttpRequestModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String sessionid;
	private Map<String,String> headers = new HashMap<String,String>();
	private Cookie[] cookies;
	
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public Cookie[] getCookies() {
		return cookies;
	}
	public void setCookies(Cookie[] cookies) {
		this.cookies = cookies;
	}
	/**
	 * @return the sessionid
	 */
	public String getSessionid() {
		return sessionid;
	}
	/**
	 * @param sessionid the sessionid to set
	 */
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	
	
}
