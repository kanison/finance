/**
 * 
 */
package com.tenpay.sm.context.scope;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.core.CollectionFactory;

import com.tenpay.sm.context.Context;
import com.tenpay.sm.context.ContextUtil;

/**
 * @author li.hongtl
 *
 */
public class ConversationScope implements Scope {
	private static final Log LOG = LogFactory.getLog(ConversationScope.class);
	public static final String NAME_CONVERSATION_CURRENT = "conversation";
	//todo
	private final Map requestDestructionCallbacks = CollectionFactory.createLinkedCaseInsensitiveMapIfPossible(8);
	
	public Object get(String name, ObjectFactory objectFactory) {
		Context ctx = ContextUtil.getContext();
		Object scopedObject = ctx.getConversation().get(name);
		if(scopedObject==null) {
			if(LOG.isDebugEnabled()) {
				LOG.debug("ConversationScope开始创建对象." + name);
			}
			scopedObject = objectFactory.getObject();
			ctx.getConversation().setAttribute(name, (java.io.Serializable)scopedObject);
			if(LOG.isDebugEnabled()) {
				LOG.debug("ConversationScope创建对象成功." + name);
			}
		}
		return scopedObject;
	}

	public String getConversationId() {
		return null;
	}

	public void registerDestructionCallback(String name, Runnable obj) {
		requestDestructionCallbacks.put(name, obj);
	}

	public Object remove(String name) {
		Context ctx = ContextUtil.getContext();
		Object scopedObject = ctx.getConversation().get(name);
		ctx.getConversation().removeAttribute(name);
		if(LOG.isDebugEnabled()) {
			LOG.debug("ConversationScope移除对象." + name);
		}
		return scopedObject;
	}

}
