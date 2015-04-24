package com.zhaocb.common.signature.imp;

import java.security.Key;

public class MD5Key implements Key {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3743212716826787556L;
	private String keyString;
	
	@SuppressWarnings("unused")
	private MD5Key()
	{
		
	}
	
	public MD5Key(String key)
	{
		this.keyString = key;
	}
	public String getAlgorithm() {
		return "MD5";
	}

	public byte[] getEncoded() {
		return keyString.getBytes();
	}

	public String getFormat() {
		return keyString;
	}

}
