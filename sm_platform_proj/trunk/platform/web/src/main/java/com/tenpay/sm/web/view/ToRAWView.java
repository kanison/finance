/**
 * 
 */
package com.tenpay.sm.web.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tenpay.sm.lang.error.ErrorCode;

public class ToRAWView extends AbstractToDataView {
	private static final Log LOG = LogFactory.getLog(ToRAWView.class);

	public ToRAWView() {
		setContentType("text/html; charset=GBK");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedTemplateModel(Map model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {
			ErrorCode errorCode = (ErrorCode) model.get("errorCode");
			if (errorCode != null) {

				String charset = request.getParameter("output_charset");
				if (charset == null || charset.length() == 0)
					charset = request.getParameter("input_charset");
				if (charset == null || charset.length() == 0)
					charset = "GBK";
				response.setCharacterEncoding(charset);
				response.setStatus(206);
				response.setContentType("text/html; charset=" + charset);
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(
						errorCode.getRetcode() + "|" + errorCode.getMessage());
				response.getWriter().flush();
			}
		} catch (Throwable t) {
			LOG.warn("ToRAWView exception: ", t);
		}
	}

}
