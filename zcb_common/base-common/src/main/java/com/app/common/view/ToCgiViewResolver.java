package com.app.common.view;

import java.util.Locale;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

import com.tenpay.sm.lang.config.AppConfig;
import com.tenpay.sm.lang.config.ReloadableAppConfig;

/**
 * @author 
 *
 */
public class ToCgiViewResolver extends AbstractCachingViewResolver  implements Ordered {
	public final static String VIEW_CGI=".cgi";
	private AppConfig appConfig;
	private String cgiViewSuffixAlias=null;
	private int order = 1;
	
	public ToCgiViewResolver()
	{
		cgiViewSuffixAlias=ReloadableAppConfig.appConfig.get("cgiViewSuffixAlias");
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractCachingViewResolver#loadView(java.lang.String, java.util.Locale)
	 */
	@Override
	protected View loadView(String viewName, Locale arg1) throws Exception {
		if(viewName.endsWith(VIEW_CGI)||(cgiViewSuffixAlias!=null&&viewName.endsWith(cgiViewSuffixAlias))) {
//			return ToXMLView.instance;
			View view = (View)this.getWebApplicationContext().getBean("toCgiView");
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
	
	
}
