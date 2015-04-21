package com.zhaocb.common.signature.po;

import java.io.Serializable;

/**
 * �̻�����Ƹ�ͨ�Ľ���������Ϣ
 * 
 * @author kansonzhang
 * 
 */
public class MD5KeyPO implements Serializable{

	private static final long serialVersionUID = 1267659584165785165L;

	/**
	 * �̻����
	 */
	private String spId;

	/**
	 * �̻�����MD5Key
	 */
	private String md5Key;

	/**
	 * �̻�����MD5Key�汾��
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
