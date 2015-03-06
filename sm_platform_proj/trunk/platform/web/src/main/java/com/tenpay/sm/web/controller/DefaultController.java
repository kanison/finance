/**
 * 
 */
package com.tenpay.sm.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.util.UrlPathHelper;

/**
 * û���κ�handlerMapping����ƥ�䵽��ʱ��ʹ�õ�Controller
 * Ĭ�ϵ���urlһ����view��
 * modelû������
 * @author li.hongtl
 *
 */
public class DefaultController implements Controller {

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String servletPath = new UrlPathHelper().getServletPath(request);
		if (servletPath.startsWith("/")) {
			servletPath = servletPath.substring(1);
		}
		return new ModelAndView(servletPath);
//		if(servletPath.endsWith(".htm")) {
//			return new ModelAndView(servletPath.substring(0,servletPath.length()-4));
//		}
//		return null;
	}

}
