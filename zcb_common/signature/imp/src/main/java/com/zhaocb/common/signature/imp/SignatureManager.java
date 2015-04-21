/**
 * 
 */
package com.zhaocb.common.signature.imp;

import java.io.UnsupportedEncodingException;
import com.app.utils.URLEncoder;
import java.security.Key;
import java.util.Map;

/**
 * @author torryhong
 * 
 */
public class SignatureManager {
	private Map<String, Signature> signatures;

	/**
	 * @param content
	 * @param sign
	 * @return 验签
	 */
	public SignatureResult verifySignature(String signatureName,
			String content, String charset, String sign, Key publicKey) {
		if (publicKey == null) {
			return null;
		}
		if (publicKey.getAlgorithm().equals("TENPAY_MD5")) {
			signatureName = "TENPAY_MD5";
		}
		Signature signature = signatures.get(signatureName.toUpperCase());
		if (signature == null) {
			return SignatureResult.INVALID_SIGN_TYPE;
		}
		return signature.verifySignature(content, charset, sign, publicKey);
	}

	/**
	 * 签名 结果urlEncode
	 * 
	 * @param signatureName
	 * @param content
	 * @param charset
	 * @param privateKey
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String signWithUrlEncode(String signatureName, String content,
			String charset, Key privateKey) throws UnsupportedEncodingException {
		return URLEncoder.encode(sign(signatureName, content, charset,
				privateKey), charset);
	}

	/**
	 * 签名
	 * 
	 * @param signatureName
	 * @param content
	 * @param charset
	 * @param privateKey
	 * @return
	 */
	public String sign(String signatureName, String content, String charset,
			Key privateKey) {
		if (privateKey == null) {
			return null;
		}
		if (privateKey.getAlgorithm().equals("TENPAY_MD5")) {
			signatureName = "TENPAY_MD5";
		}
		Signature signature = signatures.get(signatureName.toUpperCase());
		if (signature == null) {
			return null;
		}
		return signature.sign(content, charset, privateKey);
	}

	/**
	 * @return the signatures
	 */
	public Map<String, Signature> getSignatures() {
		return signatures;
	}

	/**
	 * @param signatures
	 *            the signatures to set
	 */
	public void setSignatures(Map<String, Signature> signatures) {
		this.signatures = signatures;
	}
}
