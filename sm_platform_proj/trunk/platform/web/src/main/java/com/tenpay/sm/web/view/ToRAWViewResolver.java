/**
 * 
 */
package com.tenpay.sm.web.view;

import java.util.Locale;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

import com.tenpay.sm.lang.config.AppConfig;

/**
 * 如果viewName的后缀是.xml，得到toXMLView的ViewResolver
 * @author li.hongtl
 *
 */
public class ToRAWViewResolver extends AbstractCachingViewResolver  implements Ordered {
	public final static String VIEW_RAW=".raw";
	
	private int order = 1;
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractCachingViewResolver#loadView(java.lang.String, java.util.Locale)
	 */
	@Override
	protected View loadView(String viewName, Locale arg1) throws Exception {

		if(viewName.endsWith(VIEW_RAW)) {
			View view = (View)this.getWebApplicationContext().getBean("toRAWView");
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
