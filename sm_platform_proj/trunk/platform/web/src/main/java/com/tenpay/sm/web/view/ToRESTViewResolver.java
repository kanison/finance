/**
 * 
 */
package com.tenpay.sm.web.view;

import java.util.Locale;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

/**
 * @author torryhong
 *
 */
public class ToRESTViewResolver extends AbstractCachingViewResolver implements Ordered {
	public static final String VIEW_REST = ".rest"; 
	private int order = 2;
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractCachingViewResolver#loadView(java.lang.String, java.util.Locale)
	 */
	@Override
	protected View loadView(String viewName, Locale locale) throws Exception {
		if(viewName.endsWith(VIEW_REST)) {
			View view = (View)this.getWebApplicationContext().getBean("toRESTView");			
			return view;
		} 
		return null;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
}
