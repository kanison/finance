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
 * ʵ��Spring 2.5��org.springframework.web.bind.support.SessionAttributeStore����������DefaultSessionAttributeStore
 * org.springframework.web.bind.annotation.SessionAttributes���annotationĬ��ָ����bean����Session��Conversation
 * ����û���ҵ�����Conversation��ʵ�֣�����ʵ��һ��ɽկ��
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
			LOG.debug("�Ƴ�Conversation�е����ݣ�attributeName:" + storeAttributeName);
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
			LOG.debug("��ʼ��ȡConversation�е����ݣ�attributeName:" + storeAttributeName);
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
			LOG.debug("����Conversation�е����ݣ�attributeName:" + storeAttributeName + ",value:" + attributeValue);
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
