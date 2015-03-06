/**
 * 
 */
package com.app.common.view;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.jdom.output.Format;

import com.app.utils.XmlMapTransferUtil;
import com.tenpay.sm.lang.error.ErrorCode;
import com.tenpay.sm.web.view.AbstractToDataView;

/**
 * model中的数据去掉org.springframework作为key的开头的内容 生成xml ContentType是text/xml;
 * charset=GBK
 * 
 * @author 
 * 
 */
public class ToAPIView extends AbstractToDataView {
	public static final String INPUT_CHARSET_PARAM_KEY = "input_charset";
	public static final String OUTPUT_CHARSET_PARAM_KEY = "output_charset";
	public static final String DEFAULT_CHARSET = "UTF-8";
	private static final Log LOG = LogFactory.getLog(ToAPIView.class);

	public ToAPIView() {
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
		if (LOG.isDebugEnabled())
			LOG.debug("enter wechat view.");
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

		ErrorCode errorCode = (ErrorCode) modelsMap.get("errorCode");
		Object outputObj = null;
		// modelsMap = toUserDataMap(modelsMap);
		// 只认以output结尾的参数输出
		if (errorCode != null) {
			modelsMap = new HashMap();
			modelsMap.put("retcode", errorCode.getRetcode());
			modelsMap.put("retmsg", errorCode.getMessage());
		} else {
			for (Object key : modelsMap.keySet()) {
				if (((String) key).startsWith("org.springframework"))
					continue;
				String objName = ((String) key).toLowerCase();
				if (objName.endsWith("output")) {
					Object value = modelsMap.get(key);
					if (value != null) {
						outputObj = value;
					}
				}
			}
			modelsMap = null;
		}
		response.setContentType("text/plain; charset=" + charset);
		// POST和错误用xml返回
		if (modelsMap != null || outputObj != null) {
			PrintWriter pw = response.getWriter();
			Element element = null;
			if (errorCode != null) {
				// 对于String类型增加CDATA节点，不输出null值的字段
				element = XmlMapTransferUtil.format(modelsMap, "xml", false,
						charset, false);
			} else if ("POST".equals(request.getMethod())) {
				// 对于String类型增加CDATA节点，不输出null值的字段
				//String prefix = "wc";
				element = XmlMapTransferUtil.format(outputObj, "xml", false,
						charset, false);
			} 
			if (element != null) {
				response.setContentType("text/xml; charset=" + charset);
				Format format = Format.getPrettyFormat().setEncoding(charset);
				format.setExpandEmptyElements(true);
				org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter(
						format);
				out.output(element, pw);
			} else {
				StringBuffer sb = new StringBuffer();
				sb.append(outputObj);
				pw.print(sb.toString());
			}
		}
	}

}
