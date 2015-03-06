/**
 * 
 */
package com.app.common.view;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.app.utils.CommonConstants;
import com.app.utils.CommonUtil;
import com.tenpay.sm.lang.config.ReloadableAppConfig;
import com.tenpay.sm.lang.error.ErrorCode;
import com.tenpay.sm.lang.json.JSONObject;
import com.tenpay.sm.web.view.AbstractToDataView;

/**
 * 
 * @author 
 * 
 */
public class ToCgiView extends AbstractToDataView {
	private static final Log LOG = LogFactory.getLog(ToCgiView.class);

	public ToCgiView() {
		setContentType("text/javascript; charset=UTF-8");
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
		try {
			// 取输出字符集
			String input_charset = CommonUtil.trimString(request
					.getParameter(CommonConstants.OUTPUT_CHARSET_PARAM_KEY));
			if (input_charset == null)
				input_charset = CommonUtil.trimString(request
						.getParameter(CommonConstants.INPUT_CHARSET_PARAM_KEY));
			String charset;
			if (input_charset != null) {
				charset = input_charset;
			} else {
				charset = CommonConstants.DEFAULT_CHARSET;
			}
			response.setCharacterEncoding(charset);

			PrintWriter pw = response.getWriter();
			ErrorCode errorCode = (ErrorCode) modelsMap.get("errorCode");
			Object outPutDTO = null;
			boolean htmlContent;
			if (errorCode == null) {
				htmlContent = false;
				for (Object key : modelsMap.keySet()) {// 如果存在含OutPut的对象则返回该对象的xml
					if (((String) key).startsWith("org.springframework"))
						continue;
					String objName = ((String) key).toLowerCase();
					if (objName.contains("output")) {
						outPutDTO = modelsMap.get(key);
						if (objName.contains("htmlcontent"))
							htmlContent = true;
						break;
					}
				}
			} else
				htmlContent = true;
			if (htmlContent)
				response.setContentType("text/html; charset=" + charset);
			else
				response.setContentType("text/javascript; charset=" + charset);

			// 检查是否需要设置p3p头和设置cookie
			String p3p_mode = CommonUtil.trimString(request
					.getParameter(CommonConstants.P3P_MODE_PARAM_KEY));
			if (outPutDTO != null
					&& p3p_mode != null
					&& p3p_mode.equals(CommonConstants.P3P_MODE_VALUE)) {
				try {
					Class<Object>[] paramTypes = new Class[0];
					Object[] args = new Object[0];
					Method cookieInfoMethod = outPutDTO.getClass().getMethod(
							CommonConstants.GET_COOKIE_INFO, paramTypes);
					Object cookieInfoStr = null;
					if (cookieInfoMethod != null)
						cookieInfoStr = cookieInfoMethod
								.invoke(outPutDTO, args);
					if (cookieInfoStr != null) {
						response.setHeader("P3P",
								"CP=\"IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT\"");
						response.setHeader("Set-Cookie",
								String.valueOf(cookieInfoStr));
					}
				} catch (Exception e) {
					LOG.info("set p3p mode exception:", e);
				}
			}
			if (!response.containsHeader("Cache-Control")) 
				response.setHeader("Cache-Control", "no-cache");
			JSONObject js;
			if (outPutDTO != null)
				js = new JSONObject(outPutDTO);
			else
				js = new JSONObject();
			if (errorCode != null) {
				js.put("retcode", errorCode.getRetcode());
				js.put("retmsg", errorCode.getMessage());
			} else {
				if (js.opt("retcode") == null)
					js.put("retcode", "0");
				if (js.opt("retmsg") == null)
					js.put("retmsg", "OK");
			}
			String strJs = js.toString();
			String jsonObj = request.getParameter("jsonObj");
			if (jsonObj != null && jsonObj.length() > 0) {
				strJs = String.format("%s%s%s%s%s", "var ",
						trimJsonObjStr(jsonObj), " = ", strJs, ";");
			} else {
				String JsonObj = request.getParameter("JsonObj");
				if (JsonObj != null && JsonObj.length() > 0) {
					strJs = String.format("%s%s%s%s", trimJsonObjStr(JsonObj),
							"(", strJs, ");");
				}
			}
			String needDomainSet = CommonUtil.trimString(request
					.getParameter("needDomainSet"));
			if (needDomainSet != null) {
				if (needDomainSet.equals("1") || needDomainSet.equals("2")) {
					StringBuffer sb = new StringBuffer();
					String domain = CommonUtil
							.trimString(ReloadableAppConfig.appConfig
									.get("document.domain"));
					if (domain == null)
						domain = "tenpay.com";
					sb.append("document.domain = '");
					sb.append(domain);
					sb.append("';\r\n");
					sb.append(strJs);
					if (needDomainSet.equals("1")) {
						sb.insert(0, "<script type=\"text/javascript\">\r\n");
						sb.append("\r\n</script>");
					}
					strJs = sb.toString();
				}
			}
			pw.print(strJs);

		} catch (Throwable t) {
			LOG.warn("ToCgiView exception: ", t);
		}
	}

	// 防注入，限制字符集
	private String trimJsonObjStr(String jsonObj) {
		if (jsonObj == null)
			return null;
		char[] charArray = jsonObj.toCharArray();
		int length = charArray.length;
		boolean changed = false;
		for (int i = 0; i < length; i++) {
			char character = charArray[i];
			if (character >= '0' && character <= '9' || character >= 'A'
					&& character <= 'z' || character == '.' || character == '_')
				continue;
			charArray[i] = '_';
			changed = true;
		}
		if (changed)
			jsonObj = String.valueOf(charArray);
		return jsonObj;
	}
}
