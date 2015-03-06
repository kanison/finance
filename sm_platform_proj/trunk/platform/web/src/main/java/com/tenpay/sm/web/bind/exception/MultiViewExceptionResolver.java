/**
 * 
 */
package com.tenpay.sm.web.bind.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.RequestToViewNameTranslator;

import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.lang.error.ErrorCode;
import com.tenpay.sm.lang.error.ErrorCodeException;
import com.tenpay.sm.web.context.WebModuleContext;
import com.tenpay.sm.web.locale.LocaleRequestToViewNameTranslator;

/**
 * @author torryhong
 * 
 */
public class MultiViewExceptionResolver implements HandlerExceptionResolver,
		ApplicationContextAware {
	private static final Logger LOG = Logger
			.getLogger(MultiViewExceptionResolver.class);
	private ApplicationContext applicationContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.HandlerExceptionResolver#resolveException
	 * (javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object,
	 * java.lang.Exception)
	 */
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView mv = new ModelAndView();
		//mv.addObject("exception", ex);
		ErrorCode errorCode = null;
		if (ex instanceof ErrorCodeException) {
			errorCode = ((ErrorCodeException) ex).getErrorCode();
		} else {
			errorCode = ErrorCode.code("exception", ex.getClass()
					.getSimpleName(), new String[] { ex.getMessage() });
		}
		errorCode = ErrorCodeTranslator.translateErrorCode(request,errorCode);
		mv.addObject("errorCode", errorCode);
		if (errorCode != null) {
			WebModuleContext webContext = (WebModuleContext) ContextUtil
					.getContext();
			if (webContext != null) {
				webContext.setCurrentAttribute("errorCode", errorCode);
			}
		}
		RequestToViewNameTranslator viewNameTranslator = (RequestToViewNameTranslator) this.applicationContext
				.getBean("viewNameTranslator");
		String viewName = null;
		String errorViewName = "common/error";
		try {
			viewName = viewNameTranslator.getViewName(request);
			if (viewNameTranslator instanceof LocaleRequestToViewNameTranslator) {
				if (!((LocaleRequestToViewNameTranslator) viewNameTranslator)
						.isSkipLocale()) {
					errorViewName = viewName.substring(0, 6) + errorViewName;
				}
			}
			int lastDotIndex = viewName.lastIndexOf('.');
			if (lastDotIndex >= 0) {
				errorViewName = errorViewName
						+ viewName.substring(lastDotIndex);
			}
		} catch (Exception e) {
			errorViewName = errorViewName + ".xml";
			LOG.error("获取ExceptionViewName的时候出现异常", e);
		}
		mv.setViewName(errorViewName);

		return mv;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
