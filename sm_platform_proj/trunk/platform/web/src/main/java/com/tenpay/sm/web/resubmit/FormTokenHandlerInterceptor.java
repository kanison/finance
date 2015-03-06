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
 * �����Ƿ��ظ��ύ��HandlerInterceptor
 * �����ڵ�ǰ�������з�һ��FormTokenPlugin�������ͳһ����FormToken����Ҫ��ҳ��htmlԪ��
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
//				���������������ƣ���Ȼһ��ҳ���п��ܰ����˶��ģ�飬�¸�ģ�黹Ҫ��֤������token��key��һ��
				//ֻ��������ĵ�ֱ������Ĳ���include�ģ�����forward�ģ���Ҫ����token
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
				//���������������ƣ���Ȼһ��ҳ���п��ܰ����˶��ģ�飬�¸�ģ�黹Ҫ��֤������token��key��һ��
				boolean valid = formTokenCircle.isValid(token,false);
				if(!valid) {
					throw new ReSubmitException("�����ظ��ύ��!");
				}
			}
		}
		return true;
	}

}
