/**
 * 
 */
package com.tenpay.sm.context.conversation;

import java.io.Serializable;
import java.util.Map;

/**
 * @author li.hongtl
 * Current < Request < Conversation < Session < Application
 * “对话”作用域
 */
public interface Conversation extends Map<String,Serializable> {
	/**
	 * 初始化
	 * @param requestParameter
	 * @return
	 */
	Conversation init(String requestParameter);
	
	/**
	 * “对话”id
	 * @return
	 */
	String getConversationId();
	
	/**
	 * 设置属性
	 * @param key
	 * @param value
	 */
	void setAttribute(String key,  java.io.Serializable value);
	
	/**
	 * 获得属性
	 * @param key
	 * @return
	 */
	Serializable getAttribute(String key);
	
	/**
	 * 移除属性
	 * @param key
	 */
	void removeAttribute(String key);
	
	/**
	 * 获得属性的key,value Map
	 * @return
	 */
	Map<String,Serializable> getAttributeMap();
}
