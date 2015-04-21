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
	 * ÑéÇ©
	 */
	public SignatureResult verifySignature(String content, String charset, String sign, Key publicKey);
	
	/**
	 * @param content
	 * @return
	 * Ç©Ãû
	 */
	public String sign(String content, String charset,Key privateKey);
	
}
