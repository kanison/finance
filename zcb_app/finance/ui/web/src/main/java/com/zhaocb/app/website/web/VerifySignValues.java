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

import com.app.utils.CommonConstants;
import com.app.utils.CommonUtil;
import com.zhaocb.app.website.web.model.CommonOutput;
import com.zhaocb.app.website.web.model.SignValuesOutput;
import com.zhaocb.common.signature.facade.SignatureServiceFacade;
import com.zhaocb.common.signature.facade.VerifySignatureParams;

/**
 * @author eniacli 显示支付页面
 */
@RequestMapping
public class VerifySignValues {
	private static final Log LOG = LogFactory.getLog(VerifySignValues.class);
	private SignatureServiceFacade signatureServiceFacade;

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public CommonOutput handleSignValues() throws NoSuchAlgorithmException,
			IOException {
		CommonOutput commonOutput = new CommonOutput();
		commonOutput.setRetCode(-1);
		SignValuesOutput signValuesOutput = new SignValuesOutput();
		if (signatureServiceFacade == null) {
			LOG.warn("signatureServiceFacade is null!");
			commonOutput.setRetMsg("signatureServiceFacade is null!");
			return commonOutput;
		}
		HttpServletRequest request = CommonUtil.getHttpServletRequest();
		if (request == null) {
			LOG.warn("request is null!");
			commonOutput.setRetMsg("request is null!");
			return commonOutput;
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
			commonOutput.setRetMsg("partner is null!");
			return commonOutput;
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

		VerifySignatureParams verifySignParams = new VerifySignatureParams();
		verifySignParams.setChnid(partner);
		verifySignParams.setCharset(charset);
		verifySignParams.setSign(request.getParameter("sign"));
		String signType = request
				.getParameter(CommonConstants.SIGN_TYPE_PARAM_KEY);
		if (signType != null && signType.length() > 0)
			verifySignParams.setSignType(signType);
		String signKeyIndex = request
				.getParameter(CommonConstants.SIGN_KEY_INDEX_PARAM_KEY);
		if (signKeyIndex != null && signKeyIndex.length() > 0)
			verifySignParams.setKeyIndex(Integer.valueOf(signKeyIndex));
		String signUseStr;
		try {
			signUseStr = CommonUtil.formatQueryParaMap(sortedParams, false,
					charset);
		} catch (Exception e1) {
			LOG.warn("formatQueryParaMap exception", e1);
			commonOutput.setRetMsg("formatQueryParaMap exception");
			return commonOutput;
		}
		verifySignParams.setContent(signUseStr);
		boolean signResult = signatureServiceFacade.verifySignature(verifySignParams);
		
		if(!signResult){
			LOG.info("签名验证失败");
			return null;
		}
		
		return new CommonOutput();

	}

	public SignatureServiceFacade getSignatureServiceFacade() {
		return signatureServiceFacade;
	}

	public void setSignatureServiceFacade(
			SignatureServiceFacade signatureServiceFacade) {
		this.signatureServiceFacade = signatureServiceFacade;
	}

}
