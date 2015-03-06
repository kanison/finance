/**
 * 
 */
package com.tenpay.sm.web.view;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tenpay.sm.lang.xo.XMLSerializer;

/**
 * model中的数据去掉org.springframework作为key的开头的内容 生成xml ContentType是text/xml;
 * charset=UTF-8
 * 
 * @author li.hongtl
 * 
 */
public class ToXMLView extends AbstractToDataView {
	public static final String INPUT_CHARSET_PARAM_KEY = "input_charset";
	public static final String OUTPUT_CHARSET_PARAM_KEY = "output_charset";
	public static final String DEFAULT_CHARSET = "UTF-8";

	public ToXMLView() {
		setContentType("text/xml; charset=UTF-8");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel
	 * (java.util.Map, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedTemplateModel(Map modelsMap,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 取输出字符集
		String input_charset = request.getParameter(OUTPUT_CHARSET_PARAM_KEY);
		if (input_charset == null
				|| (input_charset = input_charset.trim()).length() == 0) {
			input_charset = request.getParameter(INPUT_CHARSET_PARAM_KEY);
			if (input_charset != null)
				input_charset = input_charset.trim();
		}
		String charset;
		if (input_charset != null && input_charset.length() > 0) {
			charset = input_charset;
		} else {
			charset = DEFAULT_CHARSET;
		}
		response.setCharacterEncoding(charset);
		response.setContentType("text/xml; charset=" + charset);
		response.setHeader("Cache-Control", "no-cache");
		modelsMap = toUserDataMap(modelsMap);

		if (modelsMap != null) {
			PrintWriter pw = response.getWriter();
			XMLSerializer.serialize(modelsMap, pw, "model", false, charset);

		}
	}

}
