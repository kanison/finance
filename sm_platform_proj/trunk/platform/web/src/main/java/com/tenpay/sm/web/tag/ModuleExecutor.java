/**
 * 
 */
package com.tenpay.sm.web.tag;

import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletRequest;

import org.apache.commons.httpclient.Cookie;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.WebUtils;

import com.tenpay.sm.client.facade.HttpRequestModel;
import com.tenpay.sm.client.facade.WebContentFacade;
import com.tenpay.sm.client.impl.WebContentFacadeClientImpl;
import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.lang.config.PropertiesBean;
import com.tenpay.sm.web.context.WebModuleContext;
import com.tenpay.sm.web.mashup.MashupContentGetFacade;
import com.tenpay.sm.web.mashupapi.WebContentFacadeClientImplInServer;

/**
 * ģ���ִ����
 * @author li.hongtl
 *
 */
public class ModuleExecutor 
{
	public static String REQUEST_MODULE_SERVLET_PATH_ATTRIBUTE = ModuleExecutor.class.getName() + ".servlet_path";
	public static String REQUEST_MODULE_HANDLER_ATTRIBUTE = ModuleExecutor.class.getName() + ".handler";
	
	/**
	 * ģ���ִ�У������ RequestDispatcher ��include��forward���ߵ��ص�
	 * ʹ��include����include��ģ�����õ�request�����������治�ɼ�
	 * ʹ��forward�����ƻ������ģ���web content generate
	 * ����ʹ��MockHttpServletResponseȥforward��������������ߵ��ص�
	 * ��ģ�����õ�request�������������ÿɼ������Ҳ��ƻ������ģ���web content generate
	 * ����ʹ����MockHttpServletResponse����ģ����redirect�����Ч
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String execute(String path) throws Exception {
		WebModuleContext wc = (WebModuleContext) ContextUtil.getContext();
		
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		
//		public static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";
//		public static final String INCLUDE_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.include.context_path";
//		public static final String INCLUDE_SERVLET_PATH_ATTRIBUTE = "javax.servlet.include.servlet_path";
//		public static final String INCLUDE_PATH_INFO_ATTRIBUTE = "javax.servlet.include.path_info";
//		public static final String INCLUDE_QUERY_STRING_ATTRIBUTE = "javax.servlet.include.query_string";
		//TODO ����4��������Ҫ������
		
		Object oldValue = wc.getRequest().getAttribute(REQUEST_MODULE_SERVLET_PATH_ATTRIBUTE);
		try {
			wc.getRequest().setAttribute(REQUEST_MODULE_SERVLET_PATH_ATTRIBUTE,path);
			//wc.servletContext.getRequestDispatcher(path).include(wc.request, mockResponse);
			wc.getServletContext().getRequestDispatcher(path).forward(wc.getRequest(), mockResponse);
		}
		finally {
			wc.getRequest().setAttribute(REQUEST_MODULE_SERVLET_PATH_ATTRIBUTE,oldValue);
		}
		
//		Map attributesSnapshot = new java.util.TreeMap();
//		Enumeration attrNames = wc.request.getAttributeNames();
//		while (attrNames.hasMoreElements()) {
//			String attrName = (String) attrNames.nextElement();
//			if (true && !attrName.startsWith("org.springframework.web.servlet")
//					&& !attrName.startsWith("javax.servlet")) {
//				attributesSnapshot.put(attrName, wc.request.getAttribute(attrName));
//			}
//		}		
//		int size = attributesSnapshot.size();
		
		return mockResponse.getContentAsString();
	}
	
	public static String include(String path) throws Exception {
		WebModuleContext wc = (WebModuleContext) ContextUtil.getContext();
		
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		wc.getServletContext().getRequestDispatcher(path).include(wc.getRequest(), mockResponse);
		return mockResponse.getContentAsString();
		
//		wc.servletContext.getRequestDispatcher(path).include(wc.request, wc.response);
//		return null;
	}
	
	
	public static String executeWithHandler(String path, Object handler) throws Exception {
		WebModuleContext wc = (WebModuleContext) ContextUtil.getContext();
		Object oldValue = wc.getRequest().getAttribute(REQUEST_MODULE_HANDLER_ATTRIBUTE);
		try {
			wc.getRequest().setAttribute(REQUEST_MODULE_HANDLER_ATTRIBUTE,handler);
			return execute(path);
		}
		finally {
			wc.getRequest().setAttribute(REQUEST_MODULE_HANDLER_ATTRIBUTE,oldValue);
		}
	}
		
	public static String includeWithHandler(String path, Object handler) throws Exception {
		WebModuleContext wc = (WebModuleContext) ContextUtil.getContext();
		Object oldValue = wc.getRequest().getAttribute(REQUEST_MODULE_HANDLER_ATTRIBUTE);
		try {
			wc.getRequest().setAttribute(REQUEST_MODULE_HANDLER_ATTRIBUTE,handler);
			return include(path);
		}
		finally {
			wc.getRequest().setAttribute(REQUEST_MODULE_HANDLER_ATTRIBUTE,oldValue);
		}
	}
			
	public static boolean isModuleExecutorRequest(ServletRequest request) {
		return (request.getAttribute(REQUEST_MODULE_SERVLET_PATH_ATTRIBUTE) != null);
	}
	
	/**
	 * ץȡ���ϵͳ��html
	 * @param resourcePath
	 * @return
	 */
	public static String mashupRestResource(String resourcePath) {
		WebModuleContext wc = (WebModuleContext) ContextUtil.getContext();
		WebContentFacade webContentFacade = (WebContentFacade)
			wc.getApplicationContext().getBean("webContentFacadeClientImplInServer");
		
		String content = webContentFacade.httpGetWebContent(resourcePath,null);
		return content;
	}
	
	/**
	 * ץȡ���ϵͳ��html
	 * @param resourcePath
	 * @return
	 */
	public static String resolveMashupResource(Object mashupResource) {
		WebModuleContext wc = (WebModuleContext) ContextUtil.getContext();
		MashupContentGetFacade mashupContentGetFacade = (MashupContentGetFacade)
			wc.getApplicationContext().getBean("mashupServerFacadeImpl");

		String content = mashupContentGetFacade.resolveContent(mashupResource);
		return content;
	}
}
