/**
 * 
 */
package com.tenpay.sm.web.model;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.web.validate.ErrorsMap;
import com.tenpay.sm.web.validate.ValidateMethodInterceptor;

/**
 * 允许除了Pojo的输入，输出的数据之外，编程的方式可以添加附加的model，并合并入ModelandView的HandlerInterceptor
 * @author li.hongtl
 *
 */
public class AdditionalHandlerInterceptor implements HandlerInterceptor {
	public static final String CURRENT_ADDITIONAL_MODEL_KEY = AdditionalHandlerInterceptor.class.getName() + ".ADDITIONAL_MODEL";

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
		
		Map additionalModel = (Map)ContextUtil.getContext().getCurrentAttribute(CURRENT_ADDITIONAL_MODEL_KEY);
		if(additionalModel!=null) {
			modelAndView.addAllObjects(additionalModel);
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		ErrorsMap errorsMap = new ErrorsMap();
		ContextUtil.getContext().setCurrentAttribute(
				ValidateMethodInterceptor.CURRENT_VALIDATE_ERRORS_MAP_KEY,errorsMap);
		return true;
	}
	
	/**
	 * 获取当期模块附加模型的静态方法
	 * @return
	 */
	public static Map<String,Object> getAdditionalModel() {
		Map<String,Object> additionalModel = (Map<String,Object>)ContextUtil.getContext().getCurrentAttribute(
				AdditionalHandlerInterceptor.CURRENT_ADDITIONAL_MODEL_KEY);
		if(additionalModel==null) {
			additionalModel = new HashMap<String,Object>();
			ContextUtil.getContext().setCurrentAttribute(
					AdditionalHandlerInterceptor.CURRENT_ADDITIONAL_MODEL_KEY,additionalModel);
		}
		return additionalModel;
	}

}
