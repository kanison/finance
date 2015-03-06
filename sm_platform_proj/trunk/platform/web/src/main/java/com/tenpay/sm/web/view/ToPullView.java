/**
 * 
 */
package com.tenpay.sm.web.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.InternalResourceView;

/**
 * 这个view不显示任何内容，只是把model中的数据pull到request中，便于jsp中嵌套这个模块的模块获得这个模块的数据
 * TODO 在使用外面模块使用AbstractTemplateView(例如velocity)的时候，这个view应该将这个模块的数据放到外面模块的templateContext中,还没有想好怎么做
 * @author li.hongtl
 *
 */
public class ToPullView extends InternalResourceView {
	@Override
	protected boolean isUrlRequired() {
		return false;
	}
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void renderMergedOutputModel(
			Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// Determine which request handle to expose to the RequestDispatcher.
		HttpServletRequest requestToExpose = getRequestToExpose(request);

		// Expose the model object as request attributes.
		exposeModelAsRequestAttributes(model, requestToExpose);

		// Expose helpers as request attributes, if any.
		exposeHelpers(requestToExpose);

//		// Determine the path for the request dispatcher.
//		String dispatcherPath = prepareForRendering(requestToExpose, response);
//
//		// Obtain a RequestDispatcher for the target resource (typically a JSP).
//		RequestDispatcher rd = requestToExpose.getRequestDispatcher(dispatcherPath);
//		if (rd == null) {
//			throw new ServletException(
//					"Could not get RequestDispatcher for [" + getUrl() + "]: check that this file exists within your WAR");
//		}

//		// If already included or response already committed, perform include, else forward.
//		if (useInclude(requestToExpose, response)) {
//			response.setContentType(getContentType());
//			if (logger.isDebugEnabled()) {
//				logger.debug("Including resource [" + getUrl() + "] in InternalResourceView '" + getBeanName() + "'");
//			}
//			rd.include(requestToExpose, response);
//		}

//		else {
//			// Note: The forwarded resource is supposed to determine the content type itself.
//			exposeForwardRequestAttributes(requestToExpose);
//			if (logger.isDebugEnabled()) {
//				logger.debug("Forwarding to resource [" + getUrl() + "] in InternalResourceView '" + getBeanName() + "'");
//			}
//			rd.forward(requestToExpose, response);
//		}
	}

}
