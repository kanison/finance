/**
 * 
 */
package com.tenpay.sm.web.locale;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.tenpay.sm.lang.config.AppConfig;



/**
 * @author torryhong
 *
 */
public class LocaleFilter implements Filter {
	private static final Log LOG = LogFactory.getLog(LocaleFilter.class);

	public static final String LOCALE_INPUT_PARAM_KEY = "locale";
	public static final String LOCALE_REQUEST_ATTRIBUTE_KEY = "locale";
	public static final String LOCALE_COOKIE_KEY = "locale";
	public static final String PARAM_ALLOW_LOCAL_KEY = "allow_locale";
	public static final String DEFAULT_LOCALE = "zh_CN";
	private FilterConfig filterConfig;
	
	private Set<String> allowLocales;
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		
		ServletContext sc = this.filterConfig.getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		Object viewNameTranslator = webApplicationContext.getBean("viewNameTranslator");
		if(viewNameTranslator!=null && viewNameTranslator instanceof LocaleRequestToViewNameTranslator) {
			((LocaleRequestToViewNameTranslator)viewNameTranslator).setSkipLocale(false);
		}
		
		String allow_local = this.filterConfig.getInitParameter(PARAM_ALLOW_LOCAL_KEY);
		if(allow_local!=null && allow_local.trim().length()>0) {
			allowLocales = new HashSet<String>();
			StringTokenizer st = new StringTokenizer(allow_local,",");
			while(st.hasMoreElements()) {
				allowLocales.add(st.nextToken());
			}
		}
	}
		
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {		
			String input_locale = ((HttpServletRequest)request).getParameter(LOCALE_INPUT_PARAM_KEY);
			String str_locale = null;
			if(input_locale!=null && !"".equals(input_locale)) {
				str_locale = input_locale;
				
				//在cookie中设置最近一次指定的locale
				ServletContext sc = this.filterConfig.getServletContext();
				WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
				AppConfig appConfig = null;
				try {
					appConfig = (AppConfig)webApplicationContext.getBean("appConfig",AppConfig.class);
				} catch(Exception ex) {
					LOG.warn("没有配置appConfig" + ex.getMessage());
				}
				Cookie cookie = new Cookie(LOCALE_COOKIE_KEY,str_locale);
				cookie.setDomain(appConfig.get("global_domain"));
				cookie.setMaxAge(60*60*24*365);
				cookie.setPath("/");
				((HttpServletResponse)response).addCookie(cookie);
				
			} else {
				Cookie[] cookies = ((HttpServletRequest)request).getCookies();
				if(cookies!=null && cookies.length>0) {
					for(Cookie cookie : cookies) {
						if(cookie!=null && LOCALE_COOKIE_KEY.equals(cookie.getName())) {
							str_locale = cookie.getValue();
							break;
						}
					}
				}
				if(str_locale==null || "".equals(str_locale)) {
					str_locale = DEFAULT_LOCALE;
				}
			} 
			
			try {
				if(allowLocales!=null && !allowLocales.isEmpty()) {
					if(!allowLocales.contains(str_locale)) {
						str_locale = DEFAULT_LOCALE;
					}
				}
				String language = str_locale.substring(0,2);
				String country = "";
				if(str_locale.length()>=5) {
					country = str_locale.substring(3, 5);
				}
				Locale locale = new Locale(language,country);
				((HttpServletRequest)request).setAttribute(LOCALE_REQUEST_ATTRIBUTE_KEY, locale);
			} catch(Exception ex) {
				LOG.warn("获取locale出错:" + str_locale,ex);
			}
		} finally {
			chain.doFilter(request, response);	
		}
	}


}
