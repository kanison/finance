/**
 * 
 */
package com.tenpay.sm.web.view;

import org.springframework.web.servlet.View;

import com.tenpay.sm.context.Context;
import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.web.context.WebModuleContext;

/**
 * ���õ�ǰģ����Ҫ��view�����ģ��ִ���꣬û�б�ķ�ʽָ��view������ָ���Ľ�������springĬ�ϵ�
 * pojo����ʱ����ҳ����ת���ַ��ĺ����õ�һ������
 * @author li.hongtl
 *
 */
public class ViewUtil {
	public static String VIEW_NAME_KEY = ViewUtil.class.getName() + ".VIEW_NAME_KEY";
	
	/**
	 * ����viewName
	 * @param viewName
	 */
	public static void setViewName(String viewName) {
		Context wc = ContextUtil.getContext();
		wc.setCurrentAttribute(VIEW_NAME_KEY, viewName);
	}
	
	/**
	 * ����View
	 * @param view
	 */
	public static void setView(View view) {
		Context wc = ContextUtil.getContext();
		wc.setCurrentAttribute(VIEW_NAME_KEY, view);
	}
	
	/**
	 * ��ǰ�Ƿ�������view
	 * @return
	 */
	public static boolean hasView() {
		Context wc = ContextUtil.getContext();
		return wc.getCurrentAttribute(VIEW_NAME_KEY)!=null;
	}
	
	/**
	 * ��õ�ǰ��View��ViewName
	 * @return
	 */
	public static Object getCurrentView() {
		Context wc = ContextUtil.getContext();
		return wc.getCurrentAttribute(VIEW_NAME_KEY);
	}
	
	/**
	 * ����ViewName������Ѿ����ˣ��Ͳ�����
	 * @param viewName
	 */
	public static void setViewNameNotOverride(String viewName) {
		Context wc = ContextUtil.getContext();
		if(wc.getCurrentAttribute(VIEW_NAME_KEY)==null) {
			wc.setCurrentAttribute(VIEW_NAME_KEY, viewName);
		}
	}
	
	/**
	 * ����View������Ѿ����ˣ��Ͳ�����
	 * @param view
	 */
	public static void setViewNotOverride(View view) {
		WebModuleContext wc = (WebModuleContext)ContextUtil.getContext();
		if(wc.getCurrentAttribute(VIEW_NAME_KEY)==null) {
			wc.setCurrentAttribute(VIEW_NAME_KEY, view);
		}
	}
	
	/**
	 * �����ǰ�Ѿ����õ�View
	 *
	 */
	public static void resetView() {
		WebModuleContext wc = (WebModuleContext)ContextUtil.getContext();
		wc.removeCurrentAttribute(VIEW_NAME_KEY);
	}
	
	/**
	 * �����ǰ�Ѿ����õ�View
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
