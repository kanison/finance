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
 * ��Ԫ���Ե�ʱ�� spring request�������bean��ģ��ʵ��
 * @author li.hongtl
 *
 */
public class RequestScopeAdaptor implements Scope {
	private static final Log LOG = LogFactory.getLog(RequestScopeAdaptor.class);
	//todo
	private final Map requestDestructionCallbacks = CollectionFactory.createLinkedCaseInsensitiveMapIfPossible(8);
	
	public Object get(String name, ObjectFactory objectFactory) {
		Context ctx = ContextUtil.getContext();
		Object scopedObject = ctx.getCurrentAttribute(name);
		if(scopedObject==null) {
			if(LOG.isDebugEnabled()) {
				LOG.debug("RequestScopeAdaptor��ʼ��������." + name);
			}
			scopedObject = objectFactory.getObject();
			ctx.setCurrentAttribute(name, scopedObject);
			if(LOG.isDebugEnabled()) {
				LOG.debug("RequestScopeAdaptor��������ɹ�." + name);
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
			LOG.debug("RequestScopeAdaptor�Ƴ�����." + name);
		}
		return scopedObject;
	}

}
