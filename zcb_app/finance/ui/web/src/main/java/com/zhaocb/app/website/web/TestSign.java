package com.zhaocb.app.website.web;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import com.app.utils.CommonConstants;
import com.app.utils.CommonUtil;
import com.tenpay.sm.web.view.ViewUtil;
import com.zhaocb.app.website.web.model.SignValuesOutput;
import com.zhaocb.common.signature.facade.SignParams;
import com.zhaocb.common.signature.facade.SignResult;
import com.zhaocb.common.signature.facade.SignatureServiceFacade;

/**
 * @author eniacli 显示支付页面
 */
@RequestMapping
public class TestSign {
	private static final Log LOG = LogFactory.getLog(TestSign.class);
	private SignatureServiceFacade signatureServiceFacade;

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public SignValuesOutput handleSignValues() throws NoSuchAlgorithmException,
			IOException {

		SignValuesOutput signValuesOutput = new SignValuesOutput();
		if (signatureServiceFacade == null) {
			LOG.warn("signatureServiceFacade is null!");
			return signValuesOutput;
		}
		HttpServletRequest request = CommonUtil.getHttpServletRequest();
		if (request == null) {
			LOG.warn("request is null!");
			return signValuesOutput;
		}
		String targetUrl = request.getParameter("TARGET_URL");
		if (targetUrl == null)
			targetUrl = "";
		if (!targetUrl.contains("?"))
			targetUrl = targetUrl + "?";
		String partner = request
				.getParameter(CommonConstants.PARTNER_PARAM_KEY);
		if (partner == null || partner.length() == 0) {
			LOG.warn("partner is null!");
			return signValuesOutput;
		}
		String input_charset = request
				.getParameter(CommonConstants.INPUT_CHARSET_PARAM_KEY);
		String charset = null;
		if (input_charset != null && input_charset.length() > 0) {
			charset = input_charset;
		} else {
			charset = CommonConstants.DEFAULT_CHARSET;
		}

		// 排序参数
		TreeMap sortedParams = new TreeMap();
		Map<String, String[]> content = request.getParameterMap();
		Iterator<String> keyIt = content.keySet().iterator();
		while (keyIt.hasNext()) {
			String key = keyIt.next();
			sortedParams.put(key, request.getParameter(key));
		}
		// 去掉不参加签名的关键字
		sortedParams.remove(CommonConstants.SIGN_PARAM_KEY);
		sortedParams.remove(CommonConstants.UPLOAD_FILE_BODY_NAME);
		sortedParams.remove("TARGET_URL");

		SignParams signParams = new SignParams();
		signParams.setChnid(partner);
		signParams.setCharset(charset);
		String signType = request
				.getParameter(CommonConstants.SIGN_TYPE_PARAM_KEY);
		if (signType != null && signType.length() > 0)
			signParams.setSignType(signType);
		String signKeyIndex = request
				.getParameter(CommonConstants.SIGN_KEY_INDEX_PARAM_KEY);
		if (signKeyIndex != null && signKeyIndex.length() > 0)
			signParams.setKeyIndex(Integer.valueOf(signKeyIndex));
		String signUseStr;
		try {
			signUseStr = CommonUtil.formatQueryParaMap(sortedParams, false,
					charset);
		} catch (Exception e1) {
			LOG.warn("formatQueryParaMap exception", e1);
			return signValuesOutput;
		}
		signParams.setContent(signUseStr);
		SignResult signResult = signatureServiceFacade.sign(signParams);
		// 如果签名返回空 再试一次
		if (signResult == null || signResult.getSign() == null
				|| signResult.getSign().length() == 0) {
			LOG.info("如果签名返回空 再试一次");
			signResult = signatureServiceFacade.sign(signParams);
		}
		if(signResult == null){
			LOG.info("签名异常");
			return null;
		}
		sortedParams.put(CommonConstants.SIGN_PARAM_KEY, signResult.getSign());
		signValuesOutput.setTargetUrl(targetUrl
				+ CommonUtil.formatQueryParaMap(sortedParams, true, charset));
		if (ViewUtil.getCurrentRequestViewType().isRenderHTML()) {
			ViewUtil.setView(new RedirectView(signValuesOutput.getTargetUrl()));
		}
		return signValuesOutput;

	}

	public SignatureServiceFacade getSignatureServiceFacade() {
		return signatureServiceFacade;
	}

	public void setSignatureServiceFacade(
			SignatureServiceFacade signatureServiceFacade) {
		this.signatureServiceFacade = signatureServiceFacade;
	}

}
