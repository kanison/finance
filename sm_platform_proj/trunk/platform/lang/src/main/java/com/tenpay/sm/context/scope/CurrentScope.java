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
public class CurrentScope implements Scope {
	private static final Log LOG = LogFactory.getLog(CurrentScope.class);
	public static final String NAME_SCOPE_CURRENT = "current";
	//todo
	private final Map requestDestructionCallbacks = CollectionFactory.createLinkedCaseInsensitiveMapIfPossible(8);
	
	public Object get(String name, ObjectFactory objectFactory) {
		Context ctx = ContextUtil.getContext();
		Object scopedObject = ctx.getCurrentAttribute(name);
		if(scopedObject==null) {
			if(LOG.isDebugEnabled()) {
				LOG.debug("CurrentScope开始创建对象." + name);
			}
			scopedObject = objectFactory.getObject();
			ctx.setCurrentAttribute(name, scopedObject);
			if(LOG.isDebugEnabled()) {
				LOG.debug("CurrentScope创建对象成功." + name);
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
		Object scopedObject = ctx.getCurrentAttribute(name);
		ctx.removeCurrentAttribute(name);
		if(LOG.isDebugEnabled()) {
			LOG.debug("CurrentScope移除对象." + name);
		}
		return scopedObject;
	}

}
