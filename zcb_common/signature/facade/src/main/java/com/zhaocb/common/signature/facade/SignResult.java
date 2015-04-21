package com.zhaocb.common.signature.facade;

import java.io.Serializable;

/**
 * 签名结果
 * 
 * @author aixxia
 * 
 */
public class SignResult implements Serializable {

	private static final long serialVersionUID = 319985100475554486L;

	private String sign;

	private String signType;

	private String charset;

	private int signKeyIndex;

	/**
	 * 商户号
	 */
	private String partner;

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
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

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

}
