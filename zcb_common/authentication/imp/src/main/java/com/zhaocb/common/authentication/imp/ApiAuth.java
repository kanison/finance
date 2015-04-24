package com.zhaocb.common.authentication.imp;

import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.app.utils.CommonConstants;
import com.app.utils.CommonUtil;
import com.zhaocb.common.authentication.facade.DetailAuthFacade;
import com.zhaocb.common.signature.facade.SignatureServiceFacade;
import com.zhaocb.common.signature.facade.VerifySignatureParams;


public class ApiAuth implements DetailAuthFacade {
	private static final Log LOG = LogFactory.getLog(ApiAuth.class);

	private SignatureServiceFacade signatureServiceFacade;

	@SuppressWarnings("unchecked")
	public boolean auth(boolean needCert, Object[] args) {
		if (signatureServiceFacade == null) {
			LOG.warn("signatureServiceFacade is null!");
			return false;
		}
		HttpServletRequest request = CommonUtil.getHttpServletRequest();
		if (request == null) {
			LOG.warn("request is null!");
			return false;
		}

		// 验证请求是否过期(可选参数)
		String expireTime = CommonUtil.trimString(request
				.getParameter(CommonConstants.EXPIRE_TIME));
		if (expireTime != null) {
			try {
				if (CommonUtil.isTimeExpired(expireTime, "yyyyMMddHHmmss")) {
					LOG.warn("Time is expired: " + expireTime);
					return false;
				}
			} catch (ParseException e) {
				LOG.warn("expireTime format exception", e);
				return false;
			}
		}

		String partner = request
				.getParameter(CommonConstants.PARTNER_PARAM_KEY);
		if (partner == null || partner.length() == 0) {
			LOG.info("partner is null!");
			return false;
		}
		String sign = request.getParameter(CommonConstants.SIGN_PARAM_KEY);
		if (sign == null || sign.length() == 0) {
			LOG.info("sign is null!");
			return false;
		}
		// 取输入的字符集
		String input_charset = request
				.getParameter(CommonConstants.INPUT_CHARSET_PARAM_KEY);
		String charset = null;
		if (input_charset != null && input_charset.length() > 0) {
			charset = input_charset;
		} else {
			charset = CommonConstants.DEFAULT_CHARSET;
		}

		// 取参数
		TreeMap<String, String> sortedParams = new TreeMap<String, String>();
		Map<String, String[]> content = request.getParameterMap();
		Iterator<String> keyIt = content.keySet().iterator();
		while (keyIt.hasNext()) {
			String key = keyIt.next();
			sortedParams.put(key, request.getParameter(key));
		}
		// 去掉不参加签名的关键字
		sortedParams.remove(CommonConstants.SIGN_PARAM_KEY);
		sortedParams.remove(CommonConstants.UPLOAD_FILE_BODY_NAME);

		// 验签
		VerifySignatureParams verifySignParams = new VerifySignatureParams();
		verifySignParams.setChnid(partner);
		verifySignParams.setCharset(charset);
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
			// 按照原始格式进行检查
			signUseStr = CommonUtil.formatQueryParaMapWithBlank(sortedParams,
					false, charset);
			LOG.debug("(with blank)sign str=["+signUseStr+"]");
		} catch (Exception e1) {
			LOG.warn("formatQueryParaMapWithBlank exception", e1);
			return false;
		}
		verifySignParams.setContent(signUseStr);
		verifySignParams.setSign(sign);
		boolean result = signatureServiceFacade
				.verifySignature(verifySignParams);
		if (result == false) {
			// 兼容处理，按照trim后格式进行检查
			try {
				signUseStr = CommonUtil.formatQueryParaMap(sortedParams, false,
						charset);
				LOG.debug("(without blank)sign str=["+signUseStr+"]");
			} catch (Exception e1) {
				LOG.warn("formatQueryParaMap exception", e1);
				return false;
			}
			verifySignParams.setContent(signUseStr);
			verifySignParams.setSign(sign);
			result = signatureServiceFacade.verifySignature(verifySignParams);
			if (result == false) {
				LOG.info("verify sign fail");
				return false;
			}
		}
		return true;

	}

	

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

}
