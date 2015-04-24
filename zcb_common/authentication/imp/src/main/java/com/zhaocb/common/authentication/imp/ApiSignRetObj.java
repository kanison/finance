package com.zhaocb.common.authentication.imp;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.app.utils.CommonConstants;
import com.app.utils.CommonUtil;
import com.tenpay.sm.context.Context;
import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.web.context.WebModuleContext;
import com.zhaocb.common.signature.facade.SignParams;
import com.zhaocb.common.signature.facade.SignResult;
import com.zhaocb.common.signature.facade.SignatureServiceFacade;

public class ApiSignRetObj {
	private static final Log LOG = LogFactory.getLog(ApiSignRetObj.class);
	private SignatureServiceFacade signatureServiceFacade;

	public SignatureServiceFacade getSignatureServiceFacade() {
		return signatureServiceFacade;
	}

	public void setSignatureServiceFacade(
			SignatureServiceFacade signatureServiceFacade) {
		if (this.signatureServiceFacade == null)
			this.signatureServiceFacade = signatureServiceFacade;
	}

	public SignatureServiceFacade getApiSignatureServiceFacade() {
		return signatureServiceFacade;
	}

	public void setApiSignatureServiceFacade(
			SignatureServiceFacade signatureServiceFacade) {
		this.signatureServiceFacade = signatureServiceFacade;
	}

	public void signRetObj(Object obj) {
		if (signatureServiceFacade == null)
			return;
		Context ctx = ContextUtil.getContext();
		if (!(ctx instanceof WebModuleContext))
			return;
		WebModuleContext webContext = (WebModuleContext) ctx;
		HttpServletRequest request = webContext.getRequest();

		if (request == null)
			return;
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
		SortedMap<String, String> resultMap = CommonUtil
				.ObjToPlainSortedMap(obj);
		String partner = getStringFromMapThenRequest(resultMap, request,
				CommonConstants.PARTNER_PARAM_KEY);

		if (partner == null) {
			LOG.error("partner is null");
			return;
		}
		SignParams signParams = new SignParams();
		signParams.setChnid(partner);
		signParams.setCharset(charset);
		String signType = getStringFromMapThenRequest(resultMap, request,
				CommonConstants.SIGN_TYPE_PARAM_KEY);
		if (signType != null)
			signParams.setSignType(signType);
		String signKeyIndex = getStringFromMapThenRequest(resultMap, request,
				CommonConstants.SIGN_KEY_INDEX_PARAM_KEY);
		if (signKeyIndex != null)
			signParams.setKeyIndex(Integer.valueOf(signKeyIndex));

		// 调整返回结果中的签名参数
		resultMap.put(CommonConstants.SERVICE_VERSION_PARAM_KEY, "1.0");
		resultMap.put(CommonConstants.INPUT_CHARSET_PARAM_KEY, signParams
				.getCharset());
		resultMap.put(CommonConstants.SIGN_KEY_INDEX_PARAM_KEY, String
				.valueOf(signParams.getKeyIndex()));
		resultMap.put(CommonConstants.SIGN_TYPE_PARAM_KEY, signParams
				.getSignType());
		if (resultMap.get("retcode") == null) {
			resultMap.put("retcode", "0");
			resultMap.put("retmsg", "OK");
		}
		String signUseStr;
		try {
			signUseStr = CommonUtil.formatQueryParaMap(resultMap, false,
					charset);
		} catch (UnsupportedEncodingException e) {
			LOG.error(partner + " unsupportedEncoding: " + charset, e);
			return;
		}
		signParams.setContent(signUseStr);
		SignResult sign = signatureServiceFacade.sign(signParams);
		if (sign != null && sign.getSign() != null) {
			resultMap.put(CommonConstants.SIGN_PARAM_KEY, sign.getSign());
		} else {
			LOG.error(partner + " sign result is null");
			return;
		}
		webContext.setCurrentAttribute(CommonConstants.API_SIGNED_OBJ_NAME,
				resultMap);
	}

	protected String getStringFromMapThenRequest(Map<String, String> map,
			HttpServletRequest request, String name) {
		String value = null;
		value = map.get(name);
		if (value == null) {
			value = request.getParameter(name);
			map.put(name, value);
		}
		return value;
	}
}
