/**
 * 
 */
package com.tenpay.sm.web.view;

import java.util.Locale;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

/**
 * 如果viewName的后缀是.pull，得到toPullView的ViewResolver
 * @author li.hongtl
 *
 */
public class ToPullViewResolver extends AbstractCachingViewResolver implements Ordered  {

	public static final String VIEW_PULL = ".pull"; 
	
	private int order = 3;
	
	@Override
	protected View loadView(String viewName, Locale arg1) throws Exception {
		if(viewName.endsWith(VIEW_PULL)) {
//			return ToJSONView.instance;
			View view = (View)this.getWebApplicationContext().getBean("toPullView");			
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
