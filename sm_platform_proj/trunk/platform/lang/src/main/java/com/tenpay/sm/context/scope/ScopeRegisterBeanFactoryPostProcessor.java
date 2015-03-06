/**
 * 
 */
package com.tenpay.sm.context.scope;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;

/**
 * @author li.hongtl
 *
 */
public class ScopeRegisterBeanFactoryPostProcessor implements
		BeanFactoryPostProcessor {
	private static final Log LOG = LogFactory.getLog(ScopeRegisterBeanFactoryPostProcessor.class);
	Map<String,Scope> scopes = new HashMap<String,Scope>();

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
	 */
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
//		beanFactory.registerScope(WebApplicationContext.SCOPE_REQUEST, new RequestScopeAdaptor());
//		beanFactory.registerScope(WebApplicationContext.SCOPE_SESSION, new SessionScopeAdaptor());
//		beanFactory.registerScope(WebApplicationContext.SCOPE_GLOBAL_SESSION, new SessionScopeAdaptor());
		for(String name : this.scopes.keySet()) {
			beanFactory.registerScope(name,scopes.get(name));
			if(LOG.isDebugEnabled()) {
				LOG.debug("×¢²á×÷ÓÃÓò:" + name + " Àà£º" + scopes.get(name).getClass().getName());
			}
		}
	}

	public Map<String, Scope> getScopes() {
		return scopes;
	}

	public void setScopes(Map<String, Scope> scopes) {
		this.scopes = scopes;
	}

	
}
