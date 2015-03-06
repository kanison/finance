/**
 * 
 */
package com.tenpay.sm.cache;

/**
 * @author li.hongtl
 * 缓存操作的接口
 */
public interface Cache {
	/**
	 * 放数据
	 * @param key
	 * @param value
	 * @return
	 */
	Object put(String key,Object value);
	/**
	 * 取数据
	 * @param key
	 * @return
	 */
	Object get(String key);
	/**
	 * 数据是否存在
	 * @param key
	 * @return
	 */
	boolean isExist(String key);
	/**
	 * 缓存中移除数据
	 * @param key
	 * @return
	 */
	Object remove(String key);
}
