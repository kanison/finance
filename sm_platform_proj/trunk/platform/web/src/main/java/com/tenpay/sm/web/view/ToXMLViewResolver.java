/**
 * 
 */
package com.tenpay.sm.web.view;

import java.util.Locale;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

/**
 * 如果viewName的后缀是.xml，得到toXMLView的ViewResolver
 * @author li.hongtl
 *
 */
public class ToXMLViewResolver extends AbstractCachingViewResolver  implements Ordered {
	public static final String VIEW_XML = ".xml"; 
	public static final String VIEW_XML_ORIGINAL = ".xmlori";
	
	private int order = 1;
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractCachingViewResolver#loadView(java.lang.String, java.util.Locale)
	 */
	@Override
	protected View loadView(String viewName, Locale arg1) throws Exception {
		if(viewName.endsWith(VIEW_XML)||viewName.endsWith(VIEW_XML_ORIGINAL)) {
//			return ToXMLView.instance;
			View view = (View)this.getWebApplicationContext().getBean("toXMLView");
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
