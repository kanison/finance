/**
 * 
 */
package com.tenpay.sm.web.view;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

/**
 * ���׺ӳ�䵽��viewResolver��������viewResolver
 * ������Ը��ݲ�ͬ�ĺ�׺��ͬʱ��һ��Ӧ����֧��jsp��velocityģ��
 * @author li.hongtl
 *
 */
public class SuffixCompositeViewResolver extends AbstractCachingViewResolver implements Ordered {
	private int order = 0;
	
	Map<String,ViewResolver> viewResolvers = new TreeMap<String,ViewResolver>();
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractCachingViewResolver#loadView(java.lang.String, java.util.Locale)
	 */
	@Override
	protected View loadView(String viewName, Locale locale) throws Exception {
		for(Map.Entry<String,ViewResolver> entry : viewResolvers.entrySet()) {
			if(viewName.endsWith(entry.getKey())) {
				ViewResolver viewResolver = entry.getValue();
				String viewNameWithoutSuffix = StringUtils.stripFilenameExtension(viewName);
				try{
				    View view =  viewResolver.resolveViewName(viewNameWithoutSuffix, locale);
				    return view;
				} catch (Exception e) {//��������view�򷵻�404����
					return new View() {
						@SuppressWarnings("unchecked")
						public void render(Map model, HttpServletRequest request,
								HttpServletResponse response) throws Exception {
							response.setStatus(404);
							response.getOutputStream().write(("view can not be found!").getBytes());
						}
						public String getContentType() {
							return "text/html";
						}
					};
				}
			}
		}
		return null;
	}
	
	public Map<String, ViewResolver> getViewResolvers() {
		return viewResolvers;
	}
	public void setViewResolvers(Map<String, ViewResolver> viewResolvers) {
		this.viewResolvers = viewResolvers;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	
}
