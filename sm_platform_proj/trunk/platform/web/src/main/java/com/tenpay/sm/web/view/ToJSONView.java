/**
 * 
 */
package com.tenpay.sm.web.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tenpay.sm.lang.json.JSONObject;

/**
 * @author li.hongtl model中的数据去掉org.springframework作为key的开头的内容 生成json
 *         text，ContentType是text/text; charset=UTF-8
 */
public class ToJSONView extends AbstractToDataView {
	public static final String INPUT_CHARSET_PARAM_KEY = "input_charset";
	public static final String OUTPUT_CHARSET_PARAM_KEY = "output_charset";
	public static final String DEFAULT_CHARSET = "UTF-8";

	public ToJSONView() {
		setContentType("text/text; charset=UTF-8");
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
		response.setContentType("text/text; charset=" + charset);
		response.setHeader("Cache-Control", "no-cache");
		modelsMap = toUserDataMap(modelsMap);
		if (modelsMap != null) {
			JSONObject js = new JSONObject(modelsMap);
			String strJs = js.toString();
			response.getWriter().print(strJs);
		}
	}
}
