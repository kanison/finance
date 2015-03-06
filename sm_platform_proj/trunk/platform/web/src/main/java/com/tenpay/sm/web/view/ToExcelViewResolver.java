package com.tenpay.sm.web.view;

import java.util.Locale;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

import com.tenpay.sm.lang.config.AppConfig;

/**
 * @author eniacli
 * 
 */
public class ToExcelViewResolver extends AbstractCachingViewResolver implements
		Ordered {
	public final static String VIEW_EXCEL = ".xls";
	private AppConfig appConfig;
	private int order = 1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.view.AbstractCachingViewResolver#loadView
	 * (java.lang.String, java.util.Locale)
	 */
	@Override
	protected View loadView(String viewName, Locale arg1) throws Exception {
		if (viewName.endsWith(VIEW_EXCEL)) {
			View view = (View) this.getWebApplicationContext().getBean(
					"toExcelView");
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
