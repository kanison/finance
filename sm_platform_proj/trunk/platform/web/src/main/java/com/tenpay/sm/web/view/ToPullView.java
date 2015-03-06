/**
 * 
 */
package com.tenpay.sm.web.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.InternalResourceView;

/**
 * ���view����ʾ�κ����ݣ�ֻ�ǰ�model�е�����pull��request�У�����jsp��Ƕ�����ģ���ģ�������ģ�������
 * TODO ��ʹ������ģ��ʹ��AbstractTemplateView(����velocity)��ʱ�����viewӦ�ý����ģ������ݷŵ�����ģ���templateContext��,��û�������ô��
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
