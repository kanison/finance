/**
 * 
 */
package com.tenpay.sm.cache;

/**
 * @author li.hongtl
 * ��������Ľӿ�
 */
public interface Cache {
	/**
	 * ������
	 * @param key
	 * @param value
	 * @return
	 */
	Object put(String key,Object value);
	/**
	 * ȡ����
	 * @param key
	 * @return
	 */
	Object get(String key);
	/**
	 * �����Ƿ����
	 * @param key
	 * @return
	 */
	boolean isExist(String key);
	/**
	 * �������Ƴ�����
	 * @param key
	 * @return
	 */
	Object remove(String key);
}
