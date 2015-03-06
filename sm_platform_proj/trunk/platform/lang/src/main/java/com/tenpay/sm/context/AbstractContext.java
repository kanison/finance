/**
 * 
 */
package com.tenpay.sm.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tenpay.sm.context.conversation.ClientConversationImpl;
import com.tenpay.sm.context.conversation.Conversation;
import com.tenpay.sm.context.conversation.ServerConversationImpl;

/**
 * Context�ĳ���ʵ�֣�ʵ����Ĭ�ϵ�Conversation
 * @author li.hongtl
 * 
 */
abstract public class AbstractContext implements Context {
	/** Log object for this class. */
    private static final Log LOG = LogFactory.getLog(AbstractContext.class);
	static public final  String CURRENT_VIEWSTATE_KEY = "viewStateScope";
	static public final  String CURRENT_SERVER_CONVERSATION_KEY = "serverConversationScope";		
	static public final  String CURRENT_CONVERSATION_KEY = "conversationScope";	

	/**
	 * ��ȡConversation��Ĭ���ǿͻ���Conversation��ʵ��
	 */
	public Conversation getConversation() {
		if(LOG.isDebugEnabled()) {
			LOG.debug("��ʼĬ�ϻ�ȡgetConversationInClient");
		}
		//TODO to get from the app-config of other config to determine Conversation In Client or Server
		Conversation conversation = this.getConversationInClient();
		this.setCurrentAttribute(CURRENT_CONVERSATION_KEY, conversation);
		return conversation;
	}
	
	/**
	 * ��ȡ�ͻ���Conversation����Ҫ��web������context�����������
	 * @return
	 */
	protected Conversation getConversationInServer() {
		Conversation conversation = (Conversation)this.getCurrentAttribute(CURRENT_SERVER_CONVERSATION_KEY);
		if(conversation==null) {
			if(LOG.isDebugEnabled()) {
				LOG.debug("��ʼ������������Conversation");
			}
			conversation = ServerConversationImpl.EMPTY_CONVERSATION;
	        this.setCurrentAttribute(CURRENT_SERVER_CONVERSATION_KEY,conversation);
		}
		return conversation;
	}
	
	/**
	 * ��ȡ�����Conversation����Ҫ��web������context�����������
	 * @return
	 */
	protected Conversation getConversationInClient() {
		Conversation vs = (Conversation)this.getCurrentAttribute(CURRENT_VIEWSTATE_KEY);
	      if (vs == null) {
			  if(LOG.isDebugEnabled()) {
				  LOG.debug("��ʼ�����ͻ���Conversation");
			  }
	          vs = new ClientConversationImpl();
	          this.setCurrentAttribute(CURRENT_VIEWSTATE_KEY,vs);
	      }
	      return vs;
	}

}
