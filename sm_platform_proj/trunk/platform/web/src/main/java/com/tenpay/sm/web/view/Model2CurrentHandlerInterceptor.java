/**
 * 
 */
package com.tenpay.sm.web.view;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tenpay.sm.context.Context;
import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.web.context.WebModuleContext;

/**
 * @author li.hongtl
 *
 */
public class Model2CurrentHandlerInterceptor implements HandlerInterceptor {

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if(modelAndView==null || modelAndView.getModel()==null) {
			return;
		}
		WebModuleContext wc = (WebModuleContext)ContextUtil.getContext();
		ModelMap model = modelAndView.getModelMap();
		if(wc.isMatchRequest()) {
			Context context = ContextUtil.getContext();
			Iterator it = model.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				if (!(entry.getKey() instanceof String)) {
					throw new IllegalArgumentException(
							"Invalid key [" + entry.getKey() + "] in model Map: only Strings allowed as model keys");
				}
				String modelName = (String) entry.getKey();
				Object modelValue = entry.getValue();
				if (modelValue != null) {
					context.setCurrentAttribute(modelName, modelValue);
				}
				else {
					context.removeCurrentAttribute(modelName);
				}
			}
		}
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}


}
