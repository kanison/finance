/**
 * 
 */
package com.tenpay.sm.web.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tenpay.sm.common.lang.diagnostic.Profiler;
import com.tenpay.sm.context.Context;
import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.web.session.HttpSessionContextImpl;

/**
 * @author li.hongtl
 *
 */
public class WebModuleContextHandlerInterceptor implements HandlerInterceptor {
	private static final Logger LOG = Logger.getLogger(WebModuleContextHandlerInterceptor.class);
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		WebModuleContext context = null;
		if(ContextUtil.hasContext()) {
			context = (WebModuleContext)ContextUtil.getContext();
		}
		if(context!=null && context.getParent()==null) {
			//TODO only store Session if the WebModuleContext is Top WebModuleContext
			Profiler.enter("´æ´¢HttpSession");
			try {
				HttpSessionContextImpl.INSTANCE.storeSession(context.getHttpSession());
			}
			catch(Exception ex) {
				LOG.error("storeSession³ö´í",ex);
			} finally {
				Profiler.release();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		WebModuleContext wc = (WebModuleContext)ContextUtil.getContext();
		wc.setHandler(handler);
		return true;
	}

}
