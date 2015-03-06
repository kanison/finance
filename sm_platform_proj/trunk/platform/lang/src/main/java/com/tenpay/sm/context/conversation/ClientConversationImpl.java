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
 * Description: Conversation客户端的实现，使用客户端的 __VIEWSTATE 隐藏域
 * 把Conversation的数据序列化，Base64编码，放在客户端
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author 洪桃李
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
	 * 初始化
	 * 反序列化客户端的参数。得到“对话”数据
	 */
	public Conversation init(String requestParameter) {
		if(this.conversationId!=null) {
			if(LOG.isDebugEnabled()) {
				LOG.debug("初始化客户端Conversation,请求参数为空");
			}
			return this;
		}
		if(LOG.isDebugEnabled()) {
			LOG.debug("初始化客户端Conversation,请求参数:" + requestParameter);
		}
		//Map<String,Serializable> oldStates = this.states;
		this.clear();
		if(requestParameter==null || "".equals(requestParameter)) {
			//TODO 要不要设置conversationId? 避免再做初始化？
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
				//TODO 故意忽略，待重新评估，需要日志
				//LOG.error("初始化客户端Conversation,读取第一个key出错，忽略.",ex2);
			}
			//this.states = new HashMap<String,Serializable>();
			while (key != null) {
				Object value = null;
				try {
					value = ois.readObject();
				} catch (Exception ex3) {
					throw new ConversationException("视图状态被破坏 key=" + key + " 消息:"
							+ ex3.getMessage(), ex3);
				}
				this.setAttribute((String) key, (Serializable) value);
				try {
					key = ois.readObject();
				} catch (Exception ex1) {
					key = null;
					//TODO 故意忽略，待重新评估，需要日志，不需要日志
					//LOG.error("初始化客户端Conversation,读取key出错，忽略.",ex1);
				}
			}
			this.conversationId = UUID.randomUUID().toString();
			return this;
		} catch (ConversationException ex) {
			////还原ViewState
			//this.states = oldStates;
			LOG.error(ex.getMessage(),ex);
			throw ex;
		} catch (Exception ex) {
			////还原ViewState
			//this.states = oldStates;
			LOG.error("视图状态被破坏",ex);
			throw new ConversationException("视图状态被破坏 " + ex.getMessage(), ex);
		}
	}

	/**
	 * 序列化变成base64编码的字符串
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
			LOG.error("序列化视图状态出错! key=" + key + " 信息:",ex);
			throw new ConversationException("序列化视图状态出错! key=" + key + " 信息:"
					+ ex.getMessage(), ex);
		}
	}


	public String getConversationId() {
		return conversationId;
	}
}
