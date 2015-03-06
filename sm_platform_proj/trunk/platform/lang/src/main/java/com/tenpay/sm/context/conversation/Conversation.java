/**
 * 
 */
package com.tenpay.sm.context.conversation;

import java.io.Serializable;
import java.util.Map;

/**
 * @author li.hongtl
 * Current < Request < Conversation < Session < Application
 * ���Ի���������
 */
public interface Conversation extends Map<String,Serializable> {
	/**
	 * ��ʼ��
	 * @param requestParameter
	 * @return
	 */
	Conversation init(String requestParameter);
	
	/**
	 * ���Ի���id
	 * @return
	 */
	String getConversationId();
	
	/**
	 * ��������
	 * @param key
	 * @param value
	 */
	void setAttribute(String key,  java.io.Serializable value);
	
	/**
	 * �������
	 * @param key
	 * @return
	 */
	Serializable getAttribute(String key);
	
	/**
	 * �Ƴ�����
	 * @param key
	 */
	void removeAttribute(String key);
	
	/**
	 * ������Ե�key,value Map
	 * @return
	 */
	Map<String,Serializable> getAttributeMap();
}
