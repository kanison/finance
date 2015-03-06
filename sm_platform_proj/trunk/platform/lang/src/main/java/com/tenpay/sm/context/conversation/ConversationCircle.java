/**
 * 
 */
package com.tenpay.sm.context.conversation;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author li.hongtl
 * 服务器端Conversation的一个环，固定大小，默认5个Conversation
 * LRU淘汰机制
 */
public class ConversationCircle implements Serializable {
	private static final Log LOG = LogFactory.getLog(ConversationCircle.class);
	public static final int DEFAULT_CIRCLE_LENGTH = 5;
	private static final long serialVersionUID = -5452787744450959937L;
	
	public ConversationCircle() {
		this(DEFAULT_CIRCLE_LENGTH);
	}
	public ConversationCircle(int size) {
		conversations = new LRUMap(size);
	}
	
	private LRUMap conversations;
	
	synchronized public Conversation createConversation() {
		String conversationId = UUID.randomUUID().toString();
		if(LOG.isDebugEnabled()) {
			LOG.debug("开始创建服务器端Conversation,id: " + conversationId);
		}
		Conversation newConversation = new ServerConversationImpl(conversationId);
		conversations.put(conversationId, newConversation);

		return newConversation;
	}
	public Conversation getConversation(String conversationId) {
		Conversation conversation = (Conversation)conversations.get(conversationId);
		//TODO 先记录日志, 如果conversation为空，可能是已经过期，是否要抛?
		if(conversation==null) {
			String message = "服务器端Conversation已经作废,id:" + conversationId 
				+ "可能太多Conversation，最多允许个数:" + conversations.maxSize();
			LOG.warn(message);
			throw new ConversationException(message);
		}
		return conversation;
	}
	
//	private Map<String,Conversation> conversations = new HashMap<String,Conversation>(CIRCLE_LENGTH + 5);
//	private String[] conversationIds = new String[DEFAULT_CIRCLE_LENGTH];
//	private int currentIndex = 0;
//	
//	synchronized public Conversation getConversation(String conversationId) {
//		if(conversationId==null || "".equals(conversationId)) {
//			conversationId = UUID.randomUUID().toString();
//			Conversation newConversation = new ServerConversationImpl(conversationId);
//			conversations.put(conversationId, newConversation);
//			
//			String outConversationId = conversationIds[currentIndex];
//			conversations.remove(outConversationId);
//			
//			conversationIds[currentIndex] = conversationId;
//			currentIndex = (currentIndex + 1) % DEFAULT_CIRCLE_LENGTH;
//			return newConversation;
//		}
//		else {
//			Conversation conversation = conversations.get(conversationId);
//			//TODO 先记录日志, 如果conversation为空，可能是已经过期，是否要抛?
//			if(conversation==null) {
//				throw new ConversationException("服务器端Conversation已经作废,id:" + conversationId 
//						+ "可能太多Conversation，最多允许个数:" + DEFAULT_CIRCLE_LENGTH);
//			}
//			return conversation;
//		}
//		
//	}
}
