/**
 * 
 */
package com.tenpay.sm.cache.center;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.tenpay.sm.cache.Cache;

/**
 * @author Administrator
 *
 */
public class JavaMemCachedWrapper implements Cache {
	private MemCachedConfig memCachedConfig;
	private MemCachedClient memCachedClient;
	private SockIOPool sockIOPool;
	
	private void makesureinit() {
		if(memCachedClient==null) {
			if(memCachedConfig.getSockIOPoolName()==null) {
				sockIOPool = SockIOPool.getInstance();
			}
			else {
				sockIOPool = SockIOPool.getInstance(memCachedConfig.getSockIOPoolName());
			}
			sockIOPool.setServers(memCachedConfig.getServers());
			/**
			 * TODO memCachedConfig初始化sockIOPool
			 */
			sockIOPool.initialize();
			memCachedClient = new MemCachedClient(memCachedConfig.getSockIOPoolName());
		}
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.cache.Cache#get(java.lang.String)
	 */
	public Object get(String key) {
		this.makesureinit();
		return memCachedClient.get(key);
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.cache.Cache#isExist(java.lang.String)
	 */
	public boolean isExist(String key) {
		this.makesureinit();
		return memCachedClient.get(key)!=null;
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.cache.Cache#put(java.lang.String, java.lang.Object)
	 */
	public Object put(String key, Object value) {
		this.makesureinit();
		/**
		 * TODO 返回原来的就值memcached不支持？
		 */
		return memCachedClient.set(key, value);
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.cache.Cache#remove(java.lang.String)
	 */
	public Object remove(String key) {
		this.makesureinit();
		return memCachedClient.delete(key);
	}

	/**
	 * @return the memCachedConfig
	 */
	public MemCachedConfig getMemCachedConfig() {
		return memCachedConfig;
	}

	/**
	 * @param memCachedConfig the memCachedConfig to set
	 */
	public void setMemCachedConfig(MemCachedConfig memCachedConfig) {
		this.memCachedConfig = memCachedConfig;
	}

	/**
	 * @return the memCachedClient
	 */
	public MemCachedClient getMemCachedClient() {
		return memCachedClient;
	}


	
}
