package com.app.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.web.context.WebModuleContext;


public class SetCookieUtil {
	public static void setCookie(String name ,String value) {
		WebModuleContext ctx = (WebModuleContext) ContextUtil.getContext();
		if (ctx == null)
			return ;
		HttpServletResponse response = ctx.getResponse();
		if (response == null)
			return ;
		
		Cookie cookie = new Cookie(name,value);
		cookie.setMaxAge(3600*2);
	    response.addCookie(cookie);
	}
}
