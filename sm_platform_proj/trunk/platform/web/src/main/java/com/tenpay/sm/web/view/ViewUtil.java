/**
 * 
 */
package com.tenpay.sm.web.view;

import org.springframework.web.servlet.View;

import com.tenpay.sm.context.Context;
import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.web.context.WebModuleContext;

/**
 * 设置当前模块需要的view，如果模块执行完，没有别的方式指定view，这里指定的将优先于spring默认的
 * pojo开发时用于页面跳转，分发的很有用的一个工具
 * @author li.hongtl
 *
 */
public class ViewUtil {
	public static String VIEW_NAME_KEY = ViewUtil.class.getName() + ".VIEW_NAME_KEY";
	
	/**
	 * 设置viewName
	 * @param viewName
	 */
	public static void setViewName(String viewName) {
		Context wc = ContextUtil.getContext();
		wc.setCurrentAttribute(VIEW_NAME_KEY, viewName);
	}
	
	/**
	 * 设置View
	 * @param view
	 */
	public static void setView(View view) {
		Context wc = ContextUtil.getContext();
		wc.setCurrentAttribute(VIEW_NAME_KEY, view);
	}
	
	/**
	 * 当前是否设置了view
	 * @return
	 */
	public static boolean hasView() {
		Context wc = ContextUtil.getContext();
		return wc.getCurrentAttribute(VIEW_NAME_KEY)!=null;
	}
	
	/**
	 * 获得当前的View或ViewName
	 * @return
	 */
	public static Object getCurrentView() {
		Context wc = ContextUtil.getContext();
		return wc.getCurrentAttribute(VIEW_NAME_KEY);
	}
	
	/**
	 * 设置ViewName，如果已经有了，就不覆盖
	 * @param viewName
	 */
	public static void setViewNameNotOverride(String viewName) {
		Context wc = ContextUtil.getContext();
		if(wc.getCurrentAttribute(VIEW_NAME_KEY)==null) {
			wc.setCurrentAttribute(VIEW_NAME_KEY, viewName);
		}
	}
	
	/**
	 * 设置View，如果已经有了，就不覆盖
	 * @param view
	 */
	public static void setViewNotOverride(View view) {
		WebModuleContext wc = (WebModuleContext)ContextUtil.getContext();
		if(wc.getCurrentAttribute(VIEW_NAME_KEY)==null) {
			wc.setCurrentAttribute(VIEW_NAME_KEY, view);
		}
	}
	
	/**
	 * 清除当前已经设置的View
	 *
	 */
	public static void resetView() {
		WebModuleContext wc = (WebModuleContext)ContextUtil.getContext();
		wc.removeCurrentAttribute(VIEW_NAME_KEY);
	}
	
	/**
	 * 清除当前已经设置的View
	 *
	 */
	public static ViewType getCurrentRequestViewType() {
		WebModuleContext wc = (WebModuleContext)ContextUtil.getContext();
		String uri = wc.getRequest().getRequestURI();
		if(uri.endsWith(ToXMLViewResolver.VIEW_XML)||uri.endsWith(".xmlori")) {
			return ViewType.XML;
		}
		if(uri.endsWith(ToJSONViewResolver.VIEW_JSON)) {
			return ViewType.JSON;
		}
		if(uri.endsWith(ToRESTViewResolver.VIEW_REST)) {
			return ViewType.REST;
		}
		if(uri.endsWith(ToPullViewResolver.VIEW_PULL)) {
			return ViewType.PULL;
		}
		if(uri.endsWith(".htm")) {
			return ViewType.HTM;
		}
		if(uri.endsWith(".jhtm")) {
			return ViewType.JHTM;
		}
		if(uri.endsWith(".vhtm")) {
			return ViewType.VHTM;
		}
		if(uri.endsWith(".vhtm")) {
			return ViewType.VHTM;
		}
		if(uri.endsWith(".api")) {
			return ViewType.API;
		}
		if(uri.endsWith(".raw")) {
			return ViewType.RAW;
		}
		if(uri.endsWith(".cgi")) {
			return ViewType.CGI;
		}
		if(uri.endsWith(".xls")) {
			return ViewType.XLS;
		}
		if(uri.endsWith(".relay")) {
			return ViewType.RELAY;
		}
		return ViewType.OTHER;
	}
}
