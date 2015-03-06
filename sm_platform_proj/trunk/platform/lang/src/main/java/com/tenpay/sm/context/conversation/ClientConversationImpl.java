/**
 * 
 */
package com.tenpay.sm.context.conversation;

import java.io.*;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tenpay.sm.lang.util.Base64;

/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Conversation�ͻ��˵�ʵ�֣�ʹ�ÿͻ��˵� __VIEWSTATE ������
 * ��Conversation���������л���Base64���룬���ڿͻ���
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author ������
 * @version 1.0
 */
public class ClientConversationImpl extends HashMap<String,Serializable> implements Conversation, Serializable {
	private static final Log LOG = LogFactory.getLog(ClientConversationImpl.class);
	private static final long serialVersionUID = -6234251879884156398L;

	public ClientConversationImpl() {
	}
	private String conversationId;
	
	public void setAttribute(String key, Serializable obj) {
		this.put(key, obj);
	}

	public java.util.Iterator<String> keyIterator() {
		return this.keySet().iterator();
	}

	public Serializable getAttribute(String key) {
		return this.get(key);
	}

	public void removeAttribute(String key) {
		this.remove(key);
	}
	
	public Map<String,Serializable> getAttributeMap() {
		return this;
	}
	
	/**
	 * ��ʼ��
	 * �����л��ͻ��˵Ĳ������õ����Ի�������
	 */
	public Conversation init(String requestParameter) {
		if(this.conversationId!=null) {
			if(LOG.isDebugEnabled()) {
				LOG.debug("��ʼ���ͻ���Conversation,�������Ϊ��");
			}
			return this;
		}
		if(LOG.isDebugEnabled()) {
			LOG.debug("��ʼ���ͻ���Conversation,�������:" + requestParameter);
		}
		//Map<String,Serializable> oldStates = this.states;
		this.clear();
		if(requestParameter==null || "".equals(requestParameter)) {
			//TODO Ҫ��Ҫ����conversationId? ����������ʼ����
			return this;
		}
		try {
			java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(
					Base64.fromBase64Bytes(requestParameter));
			java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bais);
			Object key = null;
			try {
				key = ois.readObject();
			} catch (Exception ex2) {
				//TODO ������ԣ���������������Ҫ��־
				//LOG.error("��ʼ���ͻ���Conversation,��ȡ��һ��key��������.",ex2);
			}
			//this.states = new HashMap<String,Serializable>();
			while (key != null) {
				Object value = null;
				try {
					value = ois.readObject();
				} catch (Exception ex3) {
					throw new ConversationException("��ͼ״̬���ƻ� key=" + key + " ��Ϣ:"
							+ ex3.getMessage(), ex3);
				}
				this.setAttribute((String) key, (Serializable) value);
				try {
					key = ois.readObject();
				} catch (Exception ex1) {
					key = null;
					//TODO ������ԣ���������������Ҫ��־������Ҫ��־
					//LOG.error("��ʼ���ͻ���Conversation,��ȡkey��������.",ex1);
				}
			}
			this.conversationId = UUID.randomUUID().toString();
			return this;
		} catch (ConversationException ex) {
			////��ԭViewState
			//this.states = oldStates;
			LOG.error(ex.getMessage(),ex);
			throw ex;
		} catch (Exception ex) {
			////��ԭViewState
			//this.states = oldStates;
			LOG.error("��ͼ״̬���ƻ�",ex);
			throw new ConversationException("��ͼ״̬���ƻ� " + ex.getMessage(), ex);
		}
	}

	/**
	 * ���л����base64������ַ���
	 */
	public String toBase64String() {
		if(this.size()==0) {
			return null;
		}
		Object key = null;
		try {
			java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
			java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
			Iterator iter = this.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry item = (Map.Entry) iter.next();
				try {
					oos.writeObject(item.getKey());
					oos.writeObject(item.getValue());
				} catch (Exception ex1) {
					key = item.getKey();
					throw ex1;
				}
			}
			return Base64.toBase64(baos.toByteArray());
		} catch (Exception ex) {
			LOG.error("���л���ͼ״̬����! key=" + key + " ��Ϣ:",ex);
			throw new ConversationException("���л���ͼ״̬����! key=" + key + " ��Ϣ:"
					+ ex.getMessage(), ex);
		}
	}


	public String getConversationId() {
		return conversationId;
	}
}
