package com.app.common.view;

import java.util.Locale;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

import com.tenpay.sm.lang.config.AppConfig;
import com.tenpay.sm.lang.config.ReloadableAppConfig;

/**
 * 
 * @author 
 *
 */
public class ToAPIViewResolver extends AbstractCachingViewResolver  implements Ordered {
	public final static String VIEW_API=".api";
	private AppConfig appConfig;
	private String apiViewSuffixAlias=null;
	private int order = 1;
	
	public ToAPIViewResolver()
	{
		apiViewSuffixAlias=ReloadableAppConfig.appConfig.get("apiViewSuffixAlias");
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractCachingViewResolver#loadView(java.lang.String, java.util.Locale)
	 */
	@Override
	protected View loadView(String viewName, Locale arg1) throws Exception {
		if(viewName.endsWith(VIEW_API)||(apiViewSuffixAlias!=null&&viewName.endsWith(apiViewSuffixAlias))) {
//			return ToXMLView.instance;
			View view = (View)this.getWebApplicationContext().getBean("toAPIView");
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

	public AppConfig getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

	public String getApiViewSuffixAlias() {
		return apiViewSuffixAlias;
	}

	public void setApiViewSuffixAlias(String apiViewSuffixAlias) {
		this.apiViewSuffixAlias = apiViewSuffixAlias;
	}


	
	
}
