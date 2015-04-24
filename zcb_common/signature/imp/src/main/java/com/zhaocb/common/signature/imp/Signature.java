package com.zhaocb.common.signature.imp;

import java.security.Key;

/**
 * @author eniacli
 * @author torryhong
 */
public interface Signature {
	
	/**
	 * @param content
	 * @param sign
	 * @return
	 * ��ǩ
	 */
	public SignatureResult verifySignature(String content, String charset, String sign, Key publicKey);
	
	/**
	 * @param content
	 * @return
	 * ǩ��
	 */
	public String sign(String content, String charset,Key privateKey);
	
}
