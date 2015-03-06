/**
 * 
 */
package com.tenpay.sm.web.bind;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.context.request.WebRequest;

import com.tenpay.sm.context.ContextUtil;

/**
 * 实现Spring 2.5的org.springframework.web.bind.support.SessionAttributeStore，用来代替DefaultSessionAttributeStore
 * org.springframework.web.bind.annotation.SessionAttributes这个annotation默认指定把bean放入Session或Conversation
 * 但是没有找到放入Conversation的实现，这里实现一个山寨的
 * @author li.hongtl
 *
 */
public class ConversationAttributeStore implements SessionAttributeStore {
	private static final Log LOG = LogFactory.getLog(ConversationAttributeStore.class);
	private String attributeNamePrefix = "";
	
	/* (non-Javadoc)
	 * @see org.springframework.web.bind.support.SessionAttributeStore#cleanupAttribute(org.springframework.web.context.request.WebRequest, java.lang.String)
	 */
	public void cleanupAttribute(WebRequest request, String attributeName) {
		Assert.notNull(request, "WebRequest must not be null");
		Assert.notNull(attributeName, "Attribute name must not be null");
		String storeAttributeName = getAttributeNameInSession(request, attributeName);
		ContextUtil.getContext().getConversation().removeAttribute(storeAttributeName);
		if(LOG.isDebugEnabled()) {
			LOG.debug("移除Conversation中的数据，attributeName:" + storeAttributeName);
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.bind.support.SessionAttributeStore#retrieveAttribute(org.springframework.web.context.request.WebRequest, java.lang.String)
	 */
	public Object retrieveAttribute(WebRequest request, String attributeName) {
		Assert.notNull(request, "WebRequest must not be null");
		Assert.notNull(attributeName, "Attribute name must not be null");
		String storeAttributeName = getAttributeNameInSession(request, attributeName);
		if(LOG.isDebugEnabled()) {
			LOG.debug("开始获取Conversation中的数据，attributeName:" + storeAttributeName);
		}
		return ContextUtil.getContext().getConversation().getAttribute(storeAttributeName);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.bind.support.SessionAttributeStore#storeAttribute(org.springframework.web.context.request.WebRequest, java.lang.String, java.lang.Object)
	 */
	public void storeAttribute(WebRequest request, String attributeName,
			Object attributeValue) {
		Assert.notNull(request, "WebRequest must not be null");
		Assert.notNull(attributeName, "Attribute name must not be null");
		Assert.notNull(attributeValue, "Attribute value must not be null");
		String storeAttributeName = getAttributeNameInSession(request, attributeName);
		ContextUtil.getContext().getConversation().setAttribute(storeAttributeName, (Serializable)attributeValue);
		if(LOG.isDebugEnabled()) {
			LOG.debug("设置Conversation中的数据，attributeName:" + storeAttributeName + ",value:" + attributeValue);
		}
	}
	
	/**
	 * Specify a prefix to use for the attribute names in the backend session.
	 * <p>Default is to use no prefix, storing the session attributes with the
	 * same name as in the model.
	 */
	public void setAttributeNamePrefix(String attributeNamePrefix) {
		this.attributeNamePrefix = (attributeNamePrefix != null ? attributeNamePrefix : "");
	}

	/**
	 * Calculate the attribute name in the backend session.
	 * <p>The default implementation simply prepends the configured
	 * {@link #setAttributeNamePrefix "attributeNamePrefix"}, if any.
	 * @param request the current request
	 * @param attributeName the name of the attribute
	 * @return the attribute name in the backend session
	 */
	protected String getAttributeNameInSession(WebRequest request, String attributeName) {
		return this.attributeNamePrefix + attributeName;
	}
}
