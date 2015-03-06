/**
 * 
 */
package com.tenpay.sm.cache.center;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import com.tenpay.sm.cache.Cache;

import net.spy.memcached.MemcachedClient;

/**
 * @author Administrator
 *
 */
public class SpyMemCachedWrapper implements Cache {
	private MemCachedConfig memCachedConfig;
	private MemcachedClient memcachedClient;
	
	private void makesureinit() {
		if(memcachedClient==null) {
			List<InetSocketAddress> addrs = new java.util.ArrayList<InetSocketAddress>();
			for(String addr : memCachedConfig.getServers()) {
				addr = addr.trim();
				int index = addr.indexOf(':');
				addrs.add(new InetSocketAddress(addr.substring(0,index),Integer.parseInt(addr.substring(index+1))));
			}
			try {
				memcachedClient = new MemcachedClient(addrs);
			} catch (IOException e) {
				/**
				 * TODO Log init memcached connection exception
				 */
				throw new RuntimeException("初始化memcached连接异常",e);
			}
			
		}
	}
	
	/* (non-Javadoc)
	 * @see com.tenpay.sm.cache.Cache#get(java.lang.String)
	 */
	public Object get(String key) {
		this.makesureinit();
		return memcachedClient.get(key);
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.cache.Cache#isExist(java.lang.String)
	 */
	public boolean isExist(String key) {
		this.makesureinit();
		return memcachedClient.get(key)!=null;
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.cache.Cache#put(java.lang.String, java.lang.Object)
	 */
	public Object put(String key, Object value) {
		this.makesureinit();
		/**
		 * TODO 返回原来的就值memcached不支持？
		 * TODO 配置默认过期时间，默认1200秒？
		 */
		return memcachedClient.set(key,1200, value);
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.cache.Cache#remove(java.lang.String)
	 */
	public Object remove(String key) {
		this.makesureinit();
		return memcachedClient.delete(key);
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
	 * @return the memcachedClient
	 */
	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}


}
