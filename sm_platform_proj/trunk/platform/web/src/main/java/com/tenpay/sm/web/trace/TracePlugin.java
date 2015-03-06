/**
 * 
 */
package com.tenpay.sm.web.trace;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.lang.config.PropertiesBean;
import com.tenpay.sm.lang.config.ReloadableAppConfig;
import com.tenpay.sm.lang.util.ExceptionUtil;
import com.tenpay.sm.web.context.WebModuleContext;
import com.tenpay.sm.web.plugin.SmPlugin;

/**
 * @author li.hongtl
 *
 */
public class TracePlugin implements SmPlugin {
	private static final Log LOG = LogFactory.getLog(TracePlugin.class);
	public static final TracePlugin INSTANCE = new TracePlugin();
	
	public static String MODE_OFF = "0";
	public static String MODE_ON = "1";
	public static String MODE_COMMENT = "-1";
	/* (non-Javadoc)
	 * @see com.tenpay.sm.web.plugin.SmPlugin#generateContent()
	 */
	public String generateContent() {
		String web_trace_mode = ReloadableAppConfig.appConfig.get("web_trace_mode");
		if(MODE_OFF.equals(web_trace_mode)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		if(MODE_COMMENT.equals(web_trace_mode)) {
			sb.append("<!--");
		}
		WebModuleContext wc = (WebModuleContext)ContextUtil.getContext();
		generateTable(wc.getCurrentAttributesMap(),sb,"current");
		generateTable(wc.getRequest().getParameterMap(),sb,"request ParameterMap");
		generateTable(cookieMap(wc.getRequest()),sb,"request Cookie");
		generateTable(headerMap(wc.getRequest()),sb,"request Header");
		generateTable(wc.getRequest(),wc.getRequest().getAttributeNames(),sb,"request");
		generateTable(wc.getConversation().getAttributeMap(),sb,"Conversation");
		if(wc.getHttpSession()!=null)
			generateTable(wc.getHttpSession(),wc.getHttpSession().getAttributeNames(),sb,"httpSession");
		generateTable(wc.getServletContext(),wc.getServletContext().getAttributeNames(),sb,"servletContext");
		if(MODE_COMMENT.equals(web_trace_mode)) {
			sb.append("-->");
		}
		return sb.toString();
	}

	private static Map<String,org.apache.commons.httpclient.Cookie> cookieMap(HttpServletRequest request) {
		Map<String,org.apache.commons.httpclient.Cookie> map = new HashMap<String,org.apache.commons.httpclient.Cookie>();
		if(request==null || request.getCookies()==null) {
			return map;
		}
		for(int index = 0; index<request.getCookies().length; index++) {
			javax.servlet.http.Cookie servletCookie = request.getCookies()[index]; 
			org.apache.commons.httpclient.Cookie cookie = new org.apache.commons.httpclient.Cookie();
			cookie.setDomain(servletCookie.getDomain());
			cookie.setName(servletCookie.getName());
			cookie.setValue(servletCookie.getValue());
			cookie.setPath(servletCookie.getValue());
			if (servletCookie.getMaxAge() >= 0) {
				cookie.setExpiryDate(new Date(System.currentTimeMillis() + servletCookie.getMaxAge() * 1000L));
			}
			cookie.setSecure(servletCookie.getSecure());
			
			map.put(cookie.getDomain() + " : " + cookie.getName(), cookie);
		}
		return map;
	}
	
	private static Map<String,String> headerMap(HttpServletRequest request) {
		Map<String,String> map = new HashMap<String,String>();
		Enumeration en = request.getHeaderNames();
		while(en.hasMoreElements()) {
			String headName = (String) en.nextElement();
			String heanvalue = request.getHeader(headName);
			map.put(headName, heanvalue);
		}
		return map;
	}
	
	protected void generateTable(Map map,StringBuffer sb,String title) {
		sb.append("<table width='100%' border='1'><tr colspan='2'><td><font color=red>")
			.append(title)
			.append("</font></td><tr>");
		for(Object key: map.keySet()) {
			sb.append("<tr><td>")
				.append(key)
				.append("</td><td>")
				.append(toString(map.get(key)))
				.append("</td></tr>");
		}
		sb.append("</table>");
	}
	
	protected void generateTable(Object attributeScope, Enumeration enumeration,StringBuffer sb,String title) {
		sb.append("<table width='100%' border='1'><tr colspan='2'><td><font color=red>")
			.append(title)
			.append("</font></td><tr>");
		try {
			Method method = attributeScope.getClass().getMethod("getAttribute", new Class[]{String.class});
			while(enumeration.hasMoreElements()) {
				String key = (String)enumeration.nextElement();
				try {
					Object value = null;
					try {
						value = method.invoke(attributeScope, new Object[]{key});
					} catch (Exception e) {
						if(LOG.isDebugEnabled()) {
							LOG.debug("²»ÄÜgetAttribute from" + attributeScope);
						}
					} 
					sb.append("<tr><td>")
						.append(key)
						.append("</td><td>")
						.append(toString(value))
						.append("</td></tr>");
				} catch (Exception e) {
					throw ExceptionUtil.wrapException(e);
				} 
			}
		} catch (Exception e) {
			throw ExceptionUtil.wrapException(e);
		} 
		sb.append("</table>");
	}
	
	protected String toString(Object obj) {
		if(obj==null) {
			return "null";
		}
		StringBuffer sb = new StringBuffer();
		if(obj.getClass().isArray()) {
			sb.append("array: {");
			for(int index=0; index<Array.getLength(obj);index++) {
				Object item = Array.get(obj, index);
				String str = toString(item);
				sb.append(str).append(",");
			}
			sb.append("}");
			return sb.toString();
		}
		if(obj instanceof Map) {
			Map map = (Map)obj;
			sb.append("{");
			for(Object key : map.keySet()) {
				sb.append(key).append("=").append(toString(map.get(key))).append(",");
			}
			sb.append("}");
			return sb.toString();
		}
		try {
			Method method = obj.getClass().getMethod("toString", new Class[]{});
			if(method.getDeclaringClass()==Object.class) {
				return ToStringBuilder.reflectionToString(obj);
			}
			else {
				return obj.toString();
			}
		} catch (Exception e) {
			throw ExceptionUtil.wrapException(e);
		} 
	}
}
