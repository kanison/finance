/**
 * 
 */
package com.tenpay.sm.web.locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;

/**
 * @author torryhong
 *
 */
public class LocaleRequestToViewNameTranslator extends
		DefaultRequestToViewNameTranslator {
	private String defaultLocale = LocaleFilter.DEFAULT_LOCALE;
	private boolean skipLocale = true;
	/**
	 * Translates the request URI of the incoming {@link HttpServletRequest}
	 * into the view name based on the configured parameters.
	 * @see org.springframework.web.util.UrlPathHelper#getLookupPathForRequest
	 * @see #transformPath
	 */
	public String getViewName(HttpServletRequest request) {
		String viewName = super.getViewName(request);
		if(skipLocale) {
			return viewName;
		}
		Object requestLocale = request.getAttribute(LocaleFilter.LOCALE_REQUEST_ATTRIBUTE_KEY);
		if(requestLocale==null) {
			requestLocale = defaultLocale;
		}
		if(requestLocale!=null) {
			return requestLocale + "/" + viewName;
		} else {
			return viewName;
		}
	}
	
	public String getDefaultLocale() {
		return defaultLocale;
	}
	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	public boolean isSkipLocale() {
		return skipLocale;
	}

	public void setSkipLocale(boolean skipLocale) {
		this.skipLocale = skipLocale;
	}
	
	

}
