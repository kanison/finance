/**
 * 
 */
package com.tenpay.sm.client.resource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author li.hongtl
 *
 */
public class RestResource implements java.io.Serializable {
	private static final long serialVersionUID = -8430995481819193136L;
	
	private String server_config_name;
	private String uri;
	private Map<String,String> params = new HashMap<String,String>();; 


	/**
	 * @return the params
	 */
	public Map<String, String> getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	/**
	 * @return the server_config_name
	 */
	public String getServer_config_name() {
		return server_config_name;
	}

	/**
	 * @param server_config_name the server_config_name to set
	 */
	public void setServer_config_name(String server_config_name) {
		this.server_config_name = server_config_name;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}


	
}
