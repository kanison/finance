/**
 * 
 */
package com.tenpay.sm.web.mashupapi;

import java.util.Date;
import java.util.Enumeration;

import com.tenpay.sm.client.facade.HttpRequestModel;
import com.tenpay.sm.client.impl.WebContentFacadeClientImpl;
import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.lang.config.AppConfig;
import com.tenpay.sm.lang.config.PropertiesBean;
import com.tenpay.sm.web.context.WebModuleContext;

/**
 * @author li.hongtl
 *
 */
public class WebContentFacadeClientImplInServer extends
		WebContentFacadeClientImpl {
	AppConfig appConfig;
	/* (non-Javadoc)
	 * @see com.tenpay.sm.client.facade.WebContentFacade#httpGetWebContent(java.lang.String)
	 */
	public String httpGetWebContent(String path, HttpRequestModel hrm) {
		if(hrm==null) {
			hrm = new HttpRequestModel();
			if(ContextUtil.getContext() instanceof WebModuleContext) {
				WebModuleContext wc = (WebModuleContext) ContextUtil.getContext();
				Enumeration en = wc.getRequest().getHeaderNames();
				while(en.hasMoreElements()) {
					String headName = (String) en.nextElement();
					String heanvalue = wc.getRequest().getHeader(headName);
					hrm.getHeaders().put(headName, heanvalue);
				}
				
				if(wc.getRequest().getCookies()!=null) {
					org.apache.commons.httpclient.Cookie[] cookies = new org.apache.commons.httpclient.Cookie[wc.getRequest().getCookies().length];
					for(int index = 0; index<cookies.length; index++) {
						javax.servlet.http.Cookie servletCookie = wc.getRequest().getCookies()[index]; 
						cookies[index] = new org.apache.commons.httpclient.Cookie();
						cookies[index].setDomain(appConfig.get("global_domain"));
						if (!cookies[index].getDomain().startsWith(".")) {
							cookies[index].setDomain("." + cookies[index].getDomain());
						}
						cookies[index].setName(servletCookie.getName());
						cookies[index].setValue(servletCookie.getValue());
						cookies[index].setPath("/");
						if (servletCookie.getMaxAge() >= 0) {
							cookies[index].setExpiryDate(new Date(System.currentTimeMillis() + servletCookie.getMaxAge() * 1000L));
						}
						cookies[index].setSecure(servletCookie.getSecure());
					}
					hrm.setCookies(cookies);
				}
			}
		}
		return super.httpGetWebContent(path,hrm);
	}
	public AppConfig getAppConfig() {
		return appConfig;
	}
	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}
	
	
}
