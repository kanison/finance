package com.zhaocb.common.signature.imp;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.app.utils.CommonUtil;
import com.tenpay.sm.lang.config.ReloadableAppConfig;
import com.tenpay.sm.lang.util.ExceptionUtil;

/**
 * @author eniacli
 * @author torryhong
 */
public class SignatureRSAImpl implements Signature {

	private static final Logger LOG = Logger.getLogger(SignatureRSAImpl.class);
	private String signatureAlgorithm = "SHA1withRSA";
	public SignatureRSAImpl()
	{
		String algorithm=CommonUtil.trimString(ReloadableAppConfig.appConfig
		.get("signatureAlgorithm"));
		if(algorithm!=null&&algorithm.length()>0)
			this.signatureAlgorithm=algorithm;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tenpay.i18npay.common.signature.Signature#sign(byte[])
	 */
	public String sign(String content, String charset, Key privateKey) {

		try {
			java.security.Signature signer = java.security.Signature.getInstance(signatureAlgorithm);
			signer.initSign((PrivateKey) privateKey);
			signer.update(content.getBytes(charset));
			return new String(Base64.encodeBase64(signer.sign()));
		} catch (Exception e) {
			LOG.error("生成签名出错", e);
			throw ExceptionUtil.wrapException(e);
		}

	}

	public SignatureResult verifySignature(String content, String charset, String base64Sign, Key publicKey) {
		try {
			java.security.Signature signVerifier = java.security.Signature.getInstance(signatureAlgorithm);
			signVerifier.initVerify((PublicKey) publicKey);
			signVerifier.update(content.getBytes(charset));
			byte[] sign = Base64.decodeBase64(base64Sign.getBytes(charset));
			LOG.info("signStr:" + content + ",sign:" + base64Sign);
			boolean result = signVerifier.verify(sign);
			if (result == true) {
				return SignatureResult.SUCCESS;
			} else {
				return SignatureResult.FAIL;
			}
		} catch (Exception e) {
			LOG.error("验证签名出错", e);
			throw ExceptionUtil.wrapException(e);
		}
	}

}
