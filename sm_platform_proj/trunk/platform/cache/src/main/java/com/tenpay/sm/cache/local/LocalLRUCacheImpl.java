/**
 * 
 */
package com.tenpay.sm.cache.local;

import org.apache.commons.collections.map.LRUMap;
import org.springframework.beans.factory.InitializingBean;

import com.tenpay.sm.cache.Cache;

/**
 * @author li.hongtl
 *
 */
public class LocalLRUCacheImpl implements Cache, InitializingBean {
	private int maxSize = 10000;
	private LRUMap map;
	
	public void init() {
		map = new LRUMap(this.maxSize);
	}
	
	
	/* (non-Javadoc)
	 * @see com.tenpay.sm.cache.local.Cache#get(java.lang.String)
	 */
	public Object get(String key) {
		return this.map.get(key);
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.cache.local.Cache#isExist(java.lang.String)
	 */
	public boolean isExist(String key) {
		return this.map.containsKey(key);
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.cache.local.Cache#put(java.lang.String, java.lang.Object)
	 */
	public Object put(String key, Object value) {
		return this.map.put(key, value);
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.cache.local.Cache#remove(java.lang.String)
	 */
	public Object remove(String key) {
		return this.map.remove(key);
	}

	public void afterPropertiesSet() throws Exception {
		this.init();
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	
	
}
