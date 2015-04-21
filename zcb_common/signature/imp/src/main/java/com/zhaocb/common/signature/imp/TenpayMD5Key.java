package com.zhaocb.common.signature.imp;

import java.security.Key;

public class TenpayMD5Key implements Key {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3743212716826787556L;
	private String spid;

	@SuppressWarnings("unused")
	private TenpayMD5Key() {

	}

	public TenpayMD5Key(String spid) {
		this.spid = spid;
	}

	public String getAlgorithm() {
		return "TENPAY_MD5";
	}

	public byte[] getEncoded() {
		return spid.getBytes();
	}

	public String getFormat() {
		return spid;
	}

}
