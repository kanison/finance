package com.zhaocb.common.authentication.imp;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tenpay.sm.web.session.HttpServletRequestImpl;
import com.tenpay.sm.web.session.MultipartHttpServletRequestImpl;

@SuppressWarnings("unchecked")
public class AuthUtil {
	public static final String QQ_LOGIN_UIN = "qluin";
	public static final String QQ_LOGIN_UID = "qluid";
	public static final String QQ_LOGIN_SIGN = "qlsign";
	public static final String QQ_COOKIE_KEY = "qlskey";
	public static final String QQ_LOGTYPE = "qq_logtype";
	public static final String MCH_UIN = "mchqluin";
	public static final String MCH_MID = "mchqlmid";
	public static final String WEIXIN_OPENID = "uinA";
	public static final String WEIXIN_APPID = "appid";
	
	private static final Map<String, String> uidCache = java.util.Collections
			.synchronizedMap(new LRUMap(10000));
	private static final Log LOG = LogFactory.getLog(AuthUtil.class);


	private static void writeParamsToCookie(HttpServletRequest request,
			String qluin, String qluid, String qlskey) {
		Cookie cookies[] = new Cookie[] { new Cookie(QQ_LOGIN_UIN, qluin),
				new Cookie(QQ_LOGIN_UID, qluid),
				new Cookie(QQ_COOKIE_KEY, qlskey), new Cookie("from_wap", "1") };
		if (request instanceof HttpServletRequestImpl) {
			((HttpServletRequestImpl) request).setCookies(cookies);
		} else if (request instanceof MultipartHttpServletRequestImpl) {
			((MultipartHttpServletRequestImpl) request).setCookies(cookies);
		}
	}

	/**
	 * 打印请求头
	 * 
	 * @param request
	 * @param uin2
	 */
	private static void logHeaders(HttpServletRequest request, String uin2) {
		try {
			LOG.info("log headers uin:" + uin2);
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				String headerValue = request.getHeader(headerName);
				LOG.info(String.format("header name[%s], value[%s]", headerName, headerValue));
			}
		} catch (Exception ex) {
			LOG.info("log headers failed, ex:" + ex);
		}
	}

	public static String getCookieRawValue(HttpServletRequest request,
			String cookieName) {
		// 遍历所有header查找所有的cookie头，不区分大小写
		if (LOG.isDebugEnabled())
			LOG.debug("getCookieRawValue new method");
		String cookie = null;
		try {
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				if ("cookie".equalsIgnoreCase(headerName)) {
					cookie = request.getHeader(headerName);
					String value = getCookieRawValue(cookie, cookieName);
					if (value != null)
						return value;
				}
			}
		} catch (Exception ex) {
			LOG.warn("iterate request headers error.", ex);
		}

		if (LOG.isDebugEnabled())
			LOG.debug("getCookieRawValue old method");
		// 尝试老办法获取
		if (cookie == null) {
			cookie = request.getHeader("cookie");
			if (cookie == null) {
				cookie = request.getHeader("Cookie");
				if (cookie == null) {
					cookie = request.getHeader("COOKIE");
				}
			}
		}
		return getCookieRawValue(cookie, cookieName);
	}
	
	private static String getCookieRawValue(String cookie, String cookieName) {
		if (cookie != null) {
			int beginIndex = 0;
			do {
				beginIndex = cookie.indexOf(cookieName, beginIndex);
				if (beginIndex <= 0)
					break;
				char tmpChar = cookie.charAt(beginIndex - 1);
				if ((tmpChar >= '0' && tmpChar <= '9')
						|| (tmpChar >= 'A' && tmpChar <= 'z')) {
					// 存在前缀，可能不是,继续
				} else {
					int endIndex = beginIndex + cookieName.length();
					if (endIndex >= cookie.length()) {
						beginIndex = -1;
						break;
					} else {
						tmpChar = cookie.charAt(endIndex);
						if ((tmpChar >= '0' && tmpChar <= '9')
								|| (tmpChar >= 'A' && tmpChar <= 'z')) {
							// 存在后缀，可能不是,继续
						} else {
							break;
						}
					}
				}
				beginIndex = beginIndex + cookieName.length();
			} while (beginIndex < cookie.length());
			if (beginIndex >= 0 && beginIndex < cookie.length()) {
				beginIndex = cookie.indexOf("=", beginIndex
						+ cookieName.length()) + 1;
				if (beginIndex > 0 && beginIndex < cookie.length()) {
					int endIndex = cookie.indexOf(";", beginIndex);
					if (endIndex >= 0)
						return cookie.substring(beginIndex, endIndex).trim();
					else
						return cookie.substring(beginIndex).trim();
				}
			}
		}
		return null;
	}



	/**
	 * 设置请求的cookie的value
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	private static void setRequestCookieValue(HttpServletRequest request,
			String name, String value) {
		Cookie cookie = getCookie(request, name);
		if (cookie != null) {
			cookie.setValue(value);
		}
	}

	private static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie != null) {
					if (name.equals(cookie.getName())) {
						return cookie;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 向cookie中添加项，有则修改，没有则添加
	 * 
	 * @param request
	 * @param name
	 * @param value
	 */
	public static void addRequestCookieValue(HttpServletRequest request, String name,
			String value) {
		Cookie cookie = getCookie(request, name);
		if (cookie != null) {
			cookie.setValue(value);
			return;
		}
		cookie = new Cookie(name, value);
		
		Cookie[] cookies = request.getCookies();
		Cookie[] newCookies;
		if (cookies == null || cookies.length == 0) 
			newCookies = new Cookie[1];
		else {
			newCookies = new Cookie[cookies.length + 1];
			System.arraycopy(cookies, 0, newCookies, 0, cookies.length);
		}
		newCookies[newCookies.length - 1] = cookie;
		if (request instanceof HttpServletRequestImpl) {
			((HttpServletRequestImpl) request).setCookies(newCookies);
		} else if (request instanceof MultipartHttpServletRequestImpl) {
			((MultipartHttpServletRequestImpl) request).setCookies(newCookies);
		}
	}
}
