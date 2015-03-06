/**
 * 
 */
package com.tenpay.sm.web.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.web.context.WebModuleContext;

/**
 * @author torryhong 操作Cookie的简单工具
 */
public class CookieUtil {
	/**
	 * 获取请求中指定name的cookie的值。
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getValue(HttpServletRequest request, String name) {
		String value = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie != null) {
					if (name.equals(cookie.getName())) {
						value = cookie.getValue();
						break;
					}
				}
			}
		}
		return value;
	}

	public static String getValue(String name) {
		WebModuleContext ctx = (WebModuleContext) ContextUtil.getContext();
		if (ctx == null)
			return null;
		HttpServletRequest request = ctx.getRequest();
		if (request == null)
			return null;
		return getValue(request, name);
	}
}
