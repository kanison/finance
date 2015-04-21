package com.zhaocb.common.signature.imp;

import java.io.UnsupportedEncodingException;
import java.security.Key;

public class LocalTenpayMD5Key implements Key {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4016318612555406517L;
	
	private byte[] encodedMD5Key = null;
	private String charset;
	
	public LocalTenpayMD5Key(String md5Key, String charset) throws UnsupportedEncodingException {
		this.encodedMD5Key = md5Key.getBytes(charset);
		this.charset = charset;
	}

	public String getAlgorithm() {
		return "TENPAY_MD5";
	}

	public String getFormat() {
		return charset;
	}

	public byte[] getEncoded() {
		return encodedMD5Key;
	}
}
