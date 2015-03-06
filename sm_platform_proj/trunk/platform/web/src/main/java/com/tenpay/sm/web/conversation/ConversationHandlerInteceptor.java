/**
 * 
 */
package com.tenpay.sm.web.conversation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.web.context.WebModuleContext;
import com.tenpay.sm.web.tag.SmPluginExecutor;

/**
 * 在当前作用域 添加一个ConversationPlugin的HandlerInteceptor
 * 便于最后统一生成Conversation所需要的html元素
 * @author li.hongtl
 *
 */
public class ConversationHandlerInteceptor implements HandlerInterceptor {

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
		SmPluginExecutor.addPlugin(ConversationPlugin.INSTANCE);

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
//		ContextUtil.getContext().getConversation();
//		String treeNodeLocationId = request.getParameter(WebModuleContext.PARAM_TREE_NODE_LOCATION_ID);
//		if(StringUtils.hasText(treeNodeLocationId)) {
//			WebModuleContext wc = (WebModuleContext)ContextUtil.getContext();
//			if(treeNodeLocationId.equals(wc.getTreeNodeLocationId())) {
//				return true;
//			}
//			else {
//				return false;
//			}
//		}
		return true;
	}

}
