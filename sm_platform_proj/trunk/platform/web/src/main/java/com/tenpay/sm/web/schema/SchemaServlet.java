/**
 * 
 */
package com.tenpay.sm.web.schema;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.UrlPathHelper;

/**
 * @author li.hongtl
 *
 */
public class SchemaServlet extends HttpServlet {
	private static final long serialVersionUID = 4207219505348499209L;
	
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
		String beanName = StringUtils.stripFilenameExtension(lookupPath);
		
		WebApplicationContext webApplicationContext = WebApplicationContextUtils
			.getWebApplicationContext(this.getServletContext());
		
		try {
			Object bean = webApplicationContext.getBean(beanName);
			response.setContentType("text/xml; charset=GBK");
			WebModuleSchemaGenerator sg = new DefaultWebModuleSchemaGenerator();
			sg.generator(bean.getClass(), response.getWriter());
		} catch (NoSuchBeanDefinitionException e) {
			response.setContentType("text/html; charset=GBK");
			response.getWriter().println(new java.util.Formatter().format(
					"<h1>’“≤ªµΩbean£∫%s</h1>", beanName).toString());
		}

	}

}
