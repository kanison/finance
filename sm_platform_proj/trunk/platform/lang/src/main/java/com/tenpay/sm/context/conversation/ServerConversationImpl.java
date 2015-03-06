/**
 * 
 */
package com.tenpay.sm.context.conversation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.tenpay.sm.context.Context;
import com.tenpay.sm.context.ContextUtil;


/**
 * @author li.hongtl
 * ���Ի����ķ�������ʵ�֣�����session�е�һ������
 */
public class ServerConversationImpl extends HashMap<String,Serializable> implements Conversation, Serializable {
	private static final Log LOG = LogFactory.getLog(ServerConversationImpl.class);
	private static final long serialVersionUID = -7834127128174577297L;
	public static ServerConversationImpl EMPTY_CONVERSATION = new ServerConversationImpl();
	/**
	 * CONVERSATION_CIRCLE in session
	 */
	protected String CONVERSATION_CIRCLE_KEY = ServerConversationImpl.class.getName()+ ".CONVERSATION_CIRCLE";
	
	public ServerConversationImpl() {
	}
	
	public ServerConversationImpl(String conversationId) {
		this.conversationId = conversationId;
	}
	
	private String conversationId;

	public Map<String,Serializable> getAttributeMap() {
		return this;
	}
	
	/**
	 * ���ݿͻ����ύ��conversationId��ʼ��conversation��
	 * �������ȡ���е�conversation
	 * ���conversationIdΪ�մ���conversation
	 * �����ȡ���е�conversation
	 */
	public Conversation init(String requestParameter) {
		if(this.conversationId!=null && !"".equals(this.conversationId)) {
			if(LOG.isDebugEnabled()) {
				LOG.debug("��������Conversation�Ѿ���ʼ����id: " + conversationId);
			}
			return this;
		}
		
		Context context = ContextUtil.getContext();
		ConversationCircle circle = (ConversationCircle)context.getSessionAttribute(CONVERSATION_CIRCLE_KEY);
		if(circle==null) {
			if(LOG.isDebugEnabled()) {
				LOG.debug("��ʼ������ǰsession��ConversationCircle");
			}
			circle = new ConversationCircle();
			context.setSessionAttribute(CONVERSATION_CIRCLE_KEY, circle);
		}
		
		Conversation conversation = null;
		if(StringUtils.hasText(requestParameter)) {
			if(LOG.isDebugEnabled()) {
				LOG.debug("���Ի�ȡ������������Conversation��id: " + requestParameter);
			}
			conversation = circle.getConversation(requestParameter);
		}
		else {
			if(LOG.isDebugEnabled()) {
				LOG.debug("���Դ�����������Conversation");
			}
			conversation = circle.createConversation();
		}
		return conversation;
	}

	public Serializable getAttribute(String key) {
		return this.get(key);
	}

	public void removeAttribute(String key) {
		this.remove(key);
	}

	public void setAttribute(String key, Serializable value) {
		this.put(key, value);
	}

	public String getConversationId() {
		return conversationId;
	}

}
