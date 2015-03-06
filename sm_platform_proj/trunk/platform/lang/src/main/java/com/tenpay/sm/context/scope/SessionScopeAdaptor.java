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
 * ��Ԫ���Ե�ʱ�� spring session�������bean��ģ��ʵ��
 * @author li.hongtl
 *
 */
public class SessionScopeAdaptor implements Scope {
	private static final Log LOG = LogFactory.getLog(SessionScopeAdaptor.class);
	//todo
	private final Map sessionDestructionCallbacks = CollectionFactory.createLinkedCaseInsensitiveMapIfPossible(8);
	
	public Object get(String name, ObjectFactory objectFactory) {
		Context ctx = ContextUtil.getContext();
		Object scopedObject = ctx.getSessionAttribute(name);
		if(scopedObject==null) {
			if(LOG.isDebugEnabled()) {
				LOG.debug("SessionScopeAdaptor��ʼ��������." + name);
			}
			scopedObject = objectFactory.getObject();
			ctx.setSessionAttribute(name, scopedObject);
			if(LOG.isDebugEnabled()) {
				LOG.debug("SessionScopeAdaptor��������ɹ�." + name);
			}
		}
		return scopedObject;
	}

	public String getConversationId() {
		return null;
	}

	public void registerDestructionCallback(String name, Runnable obj) {
		sessionDestructionCallbacks.put(name, obj);
	}

	public Object remove(String name) {
		Context ctx = ContextUtil.getContext();
		Object scopedObject = ctx.getSessionAttribute(name);
		ctx.removeSessionAttribute(name);
		if(LOG.isDebugEnabled()) {
			LOG.debug("SessionScopeAdaptor�Ƴ�����." + name);
		}		
		return scopedObject;
	}

}
