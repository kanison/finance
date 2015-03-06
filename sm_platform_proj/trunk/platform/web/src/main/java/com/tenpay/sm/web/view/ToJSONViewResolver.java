/**
 * 
 */
package com.tenpay.sm.web.view;

import java.util.Locale;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;



/**
 * ���viewName�ĺ�׺��.json���õ�toJSONView��ViewResolver
 * @author li.hongtl
 *
 */
public class ToJSONViewResolver extends AbstractCachingViewResolver implements Ordered {
	public static final String VIEW_JSON = ".json"; 
	
	private int order = 0;
	
	@Override
	protected View loadView(String viewName, Locale arg1) throws Exception {
		if(viewName.endsWith(VIEW_JSON)) {
//			return ToJSONView.instance;
			View view = (View)this.getWebApplicationContext().getBean("toJSONView");			
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
