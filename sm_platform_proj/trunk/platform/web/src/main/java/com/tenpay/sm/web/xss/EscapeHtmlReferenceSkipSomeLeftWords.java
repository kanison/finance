/**
 * 
 */
package com.tenpay.sm.web.xss;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.web.context.WebModuleContext;


/**
 * @author torryhong
 *
 */
public class EscapeHtmlReferenceSkipSomeLeftWords extends org.apache.velocity.app.event.implement.EscapeHtmlReference
	implements ApplicationContextAware{
	private ApplicationContext applicationContext;
	private List<String> skipWords = new java.util.ArrayList<String>(java.util.Arrays.asList(new String[]{
		"${plugin.","$!{plugin.",
		"${module.","$!{module.",
		"${defaultMashupContent.","$!{defaultMashupContent.",
	}));
	
	
	/* (non-Javadoc)
	 * @see org.apache.velocity.app.event.ReferenceInsertionEventHandler#referenceInsert(java.lang.String, java.lang.Object)
	 */
	public Object referenceInsert(String reference, Object value) {
		List<String> theSkipWords = skipWords;
		
		if(this.applicationContext==null) {
			WebModuleContext webModuleContext = (WebModuleContext)ContextUtil.getContext();
			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(webModuleContext.getServletContext());
			this.applicationContext = webApplicationContext;
		}
		
		if(this.applicationContext!=null) {
			EscapeHtmlReferenceSkipSomeLeftWords it = (EscapeHtmlReferenceSkipSomeLeftWords) 
				this.applicationContext.getBean("escapeHtmlReferenceSkipSomeLeftWords");
			if(it!=null && it.getSkipWords()!=null && !it.getSkipWords().isEmpty()) {
				theSkipWords = it.getSkipWords();
			}
		}
		for(String skipWord : theSkipWords) {
			if(reference.startsWith(skipWord)) {
				return value;
			}
		}
		return super.referenceInsert(reference, value);
	}

	public List<String> getSkipWords() {
		return skipWords;
	}

	public void setSkipWords(List<String> skipWords) {
		this.skipWords = skipWords;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	
}
