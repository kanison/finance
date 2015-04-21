package com.zhaocb.common.signature.po;

import java.io.Serializable;


/**
 * 商户默认参数
 * 
 * @author eniacli
 * 
 */
public class DefaultSettingPO implements Serializable{


	private static final long serialVersionUID = -7416312854603072682L;

	private String signType;

	private String charset;

	private int signKeyIndex;
	private String partner;

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public int getSignKeyIndex() {
		return signKeyIndex;
	}

	public void setSignKeyIndex(int signKeyIndex) {
		this.signKeyIndex = signKeyIndex;
	}

}
