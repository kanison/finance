/**
 * 
 */
package com.tenpay.sm.web.resubmit;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.web.context.WebModuleContext;
import com.tenpay.sm.web.tag.ModuleExecutor;
import com.tenpay.sm.web.tag.SmPluginExecutor;

/**
 * 检查表单是否重复提交的HandlerInterceptor
 * 并且在当前作用域中放一个FormTokenPlugin便于最后统一生成FormToken所需要的页面html元素
 * @author li.hongtl
 *
 */
public class FormTokenHandlerInterceptor implements HandlerInterceptor {
	//public static final String FORM_TOKEN_KEY = FormTokenHandlerInterceptor.class.getName() + ".TOKEN";
	public static final String SESSION_FORM_TOKEN_CIRCLE_KEY = FormTokenHandlerInterceptor.class.getName() + ".TOKEN_CIRCLE";
	public static final String PARAM_FORM_TOKEN = FormTokenHandlerInterceptor.class.getName() + ".TOKEN";
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		WebModuleContext wc = (WebModuleContext)ContextUtil.getContext();
		String token = request.getParameter(PARAM_FORM_TOKEN + wc.getTreeNodeLocationId());
		if(StringUtils.hasText(token)) {
			FormTokenCircle formTokenCircle = (FormTokenCircle)request.getSession(true)
				.getAttribute(SESSION_FORM_TOKEN_CIRCLE_KEY);
			if(formTokenCircle!=null) {
//				可以马上作废令牌，虽然一个页面中可能包含了多个模块，下个模块还要验证，但是token的key不一样
				//只有最外面的的直接请求的不是include的，不是forward的，才要作废token
				if (WebUtils.isIncludeRequest(request) 
						|| ModuleExecutor.isModuleExecutorRequest(request)) {
					return;
				}
				else {
					formTokenCircle.voidToken(token);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		SmPluginExecutor.addPlugin(FormTokenPlugin.INSTANCE);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		WebModuleContext wc = (WebModuleContext)ContextUtil.getContext();
		String token = request.getParameter(PARAM_FORM_TOKEN + wc.getTreeNodeLocationId());
		if(StringUtils.hasText(token)) {
			FormTokenCircle formTokenCircle = (FormTokenCircle)request.getSession(true)
				.getAttribute(SESSION_FORM_TOKEN_CIRCLE_KEY);
			if(formTokenCircle!=null) {
				//可以马上作废令牌，虽然一个页面中可能包含了多个模块，下个模块还要验证，但是token的key不一样
				boolean valid = formTokenCircle.isValid(token,false);
				if(!valid) {
					throw new ReSubmitException("不能重复提交表单!");
				}
			}
		}
		return true;
	}

}
