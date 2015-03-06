/**
 * 
 */
package com.tenpay.sm.client.impl;




import java.util.Date;

import com.tenpay.sm.client.facade.HttpRequestModel;
import com.tenpay.sm.client.facade.WebContentFacade;
import com.tenpay.sm.lang.config.AppConfig;
import com.tenpay.sm.lang.config.PropertiesBean;
import com.tenpay.sm.lang.util.ExceptionUtil;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.httpclient.Cookie;
/**
 * @author li.hongtl
 *
 */
public class WebContentFacadeClientImpl implements WebContentFacade {
	private static final Log LOG = LogFactory.getLog(WebContentFacadeClientImpl.class);
	HostConfiguration hostConfiguration = new HostConfiguration();
	HttpClient httpClient;
	AppConfig appConfig;
	
	public WebContentFacadeClientImpl() {
		httpClient = new HttpClient();
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
	}
	
	/**
	 * 这个属性后来发现没什么用处，再看看
	 */
	private String serverAndContextURL="";
	
	
	protected void initMethodAndState(HttpMethod hm, HttpState httpState,  HttpRequestModel httpRequestModel) {
		if(httpRequestModel==null) {
			return;
		}
		
//		Iterator<String> iter = httpRequestModel.getHeaders().keySet().iterator();
//		while(iter.hasNext()) {
//			String headerName = (String) iter.next();
//			String headerValue = httpRequestModel.getHeaders().get(headerName);
//			hm.addRequestHeader(headerName, headerValue);
//		}
//		if(LOG.isDebugEnabled()) {
//			LOG.debug("initHeader, path="+hm.getPath() + " header: " + httpRequestModel.getHeaders());
//		}
		
		if(httpRequestModel.getCookies()!=null && httpState!=null) {
			httpState.setCookiePolicy(CookiePolicy.COMPATIBILITY);
			for(Cookie cookie : httpRequestModel.getCookies()) {
				if(appConfig!=null) {
					cookie.setDomain(appConfig.get("global_domain"));
				}
				cookie.setPath("/");
				cookie.setExpiryDate(new Date(System.currentTimeMillis() + 20*60 * 1000L));
				httpState.addCookie(cookie);
				if(LOG.isDebugEnabled()) {
					LOG.debug("initCookie, path="+hm.getPath() + " cookie: " + cookie);
				}
			}
		}
	}


	public String getServerAndContextURL() {
		return serverAndContextURL;
	}
	public void setServerAndContextURL(String serverAndContextURL) {
		this.serverAndContextURL = serverAndContextURL;
	}


	public String httpGetWebContent(String path, HttpRequestModel httpRequestModel) {
		
		if(serverAndContextURL.toLowerCase().startsWith("https://")) {
			//TODO 
		}
		
		GetMethod hm = new GetMethod(serverAndContextURL + path);
		hm.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		
		HttpState httpState = new HttpState();
		//hm.setPath(serverAndContextURL + path);
		//hm.setFollowRedirects(true);
		initMethodAndState(hm ,httpState, httpRequestModel);
		
		
		try {
			httpClient.executeMethod(hostConfiguration, hm, httpState);
			String content = hm.getResponseBodyAsString();
			return content;
		} catch (Exception e) {
			throw ExceptionUtil.wrapException(e);
		} 
	}


	public AppConfig getAppConfig() {
		return appConfig;
	}


	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

	
}
