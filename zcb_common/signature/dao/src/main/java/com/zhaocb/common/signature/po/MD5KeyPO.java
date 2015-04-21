package com.zhaocb.common.signature.po;

import java.io.Serializable;

/**
 * 商户接入财付通的交互配置信息
 * 
 * @author kansonzhang
 * 
 */
public class MD5KeyPO implements Serializable{

	private static final long serialVersionUID = 1267659584165785165L;

	/**
	 * 商户编号
	 */
	private String spId;

	/**
	 * 商户请求MD5Key
	 */
	private String md5Key;

	/**
	 * 商户请求MD5Key版本号
	 */
	private int keyIndex;

	public String getSpId() {
		return spId;
	}

	public void setSpId(String spId) {
		this.spId = spId;
	}

	public String getMd5Key() {
		return md5Key;
	}

	public void setMd5Key(String md5Key) {
		this.md5Key = md5Key;
	}

	public int getKeyIndex() {
		return keyIndex;
	}

	public void setKeyIndex(int keyIndex) {
		this.keyIndex = keyIndex;
	}

	
}
