package com.zhaocb.common.aop.aspect;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

import com.app.utils.CommonUtil;
import com.tenpay.sm.web.session.HttpServletRequestImpl;
import com.tenpay.sm.web.session.MultipartHttpServletRequestImpl;

/**
 * �������д�������qluin,qluid,qlskey д�뵽cookie
 * 
 * @author kansonzhang
 * 
 */
@Aspect
public class WriteParamsToCookieAspect implements Ordered {
	public static final String QQ_LOGIN_UIN = "qluin";
	public static final String QQ_LOGIN_UID = "qluid";
	public static final String QQ_COOKIE_KEY = "qlskey";
	/***
	 * ���� * �е� ����
	 */
	@Pointcut("@annotation(com.tenpay.common.aop.annotation.WriteParamsToCookie)")
	public void allAddMethod() {
	};

	/***
	 * ִ�з���
	 * 
	 * @throws Throwable
	 */
	@Around("allAddMethod()")
	public Object writeParamsToCookie(ProceedingJoinPoint joinPoint) throws Throwable {
		writeCookie();
		Object retObj = joinPoint.proceed();
		return retObj;

	}

	private void writeCookie() {
		HttpServletRequest request = CommonUtil.getHttpServletRequest();
		if(request == null)
			return;
		String qluin = CommonUtil
				.trimString(request.getParameter(QQ_LOGIN_UIN));
		String qluid = CommonUtil
				.trimString(request.getParameter(QQ_LOGIN_UID));
		String qlskey = CommonUtil.trimString(request
				.getParameter(QQ_COOKIE_KEY));

		List<Cookie> cookieList = new ArrayList<Cookie>();
		//���������Ҫ�Ѳ���д��cookieList�� ֱ���ں�����ӾͿ�����
		if(qluin != null){
			cookieList.add(new Cookie(QQ_LOGIN_UIN, qluin));
		}
		if(qluid != null){
			cookieList.add(new Cookie(QQ_LOGIN_UID, qluid));
		}
		if(qlskey != null){
			cookieList.add(new Cookie(QQ_COOKIE_KEY, qlskey));
		}
		
		Cookie cookies[] = (Cookie[])cookieList.toArray(new Cookie[cookieList.size()]);
		if (request instanceof HttpServletRequestImpl) {
			((HttpServletRequestImpl) request).setCookies(cookies);
		} else if (request instanceof MultipartHttpServletRequestImpl) {
			((MultipartHttpServletRequestImpl) request).setCookies(cookies);
		}
	}

	public int getOrder() {
		return 1;
	}

}
