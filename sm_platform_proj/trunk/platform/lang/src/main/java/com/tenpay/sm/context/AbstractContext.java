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
 * Context的抽象实现，实现了默认的Conversation
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
	 * 获取Conversation，默认是客户端Conversation的实现
	 */
	public Conversation getConversation() {
		if(LOG.isDebugEnabled()) {
			LOG.debug("开始默认获取getConversationInClient");
		}
		//TODO to get from the app-config of other config to determine Conversation In Client or Server
		Conversation conversation = this.getConversationInClient();
		this.setCurrentAttribute(CURRENT_CONVERSATION_KEY, conversation);
		return conversation;
	}
	
	/**
	 * 获取客户端Conversation，需要被web环境的context覆盖这个方法
	 * @return
	 */
	protected Conversation getConversationInServer() {
		Conversation conversation = (Conversation)this.getCurrentAttribute(CURRENT_SERVER_CONVERSATION_KEY);
		if(conversation==null) {
			if(LOG.isDebugEnabled()) {
				LOG.debug("开始创建服务器端Conversation");
			}
			conversation = ServerConversationImpl.EMPTY_CONVERSATION;
	        this.setCurrentAttribute(CURRENT_SERVER_CONVERSATION_KEY,conversation);
		}
		return conversation;
	}
	
	/**
	 * 获取服务端Conversation，需要被web环境的context覆盖这个方法
	 * @return
	 */
	protected Conversation getConversationInClient() {
		Conversation vs = (Conversation)this.getCurrentAttribute(CURRENT_VIEWSTATE_KEY);
	      if (vs == null) {
			  if(LOG.isDebugEnabled()) {
				  LOG.debug("开始创建客户端Conversation");
			  }
	          vs = new ClientConversationImpl();
	          this.setCurrentAttribute(CURRENT_VIEWSTATE_KEY,vs);
	      }
	      return vs;
	}

}
