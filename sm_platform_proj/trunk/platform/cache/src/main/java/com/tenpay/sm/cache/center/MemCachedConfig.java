/**
 * 
 */
package com.tenpay.sm.cache.center;

/**
 * @author Administrator
 *
 */
public class MemCachedConfig {
	private String sockIOPoolName;
	private String[] servers;

	/**
	 * @return the sockIOPoolName
	 */
	public String getSockIOPoolName() {
		return sockIOPoolName;
	}

	/**
	 * @param sockIOPoolName the sockIOPoolName to set
	 */
	public void setSockIOPoolName(String sockIOPoolName) {
		this.sockIOPoolName = sockIOPoolName;
	}

	/**
	 * @return the servers
	 */
	public String[] getServers() {
		return servers;
	}

	/**
	 * @param servers the servers to set
	 */
	public void setServers(String[] servers) {
		this.servers = servers;
	}
	
	
}
