package com.zhaocb.common.signature.facade;

import java.io.Serializable;

public class VerifySignatureParams implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5844247186456822284L;
	private String content;
	private String sign;
	private String chnid;
	private String signType = "MD5";
	private int keyIndex = 1;
	private String charset = "GBK";

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getChnid() {
		return chnid;
	}
	public void setChnid(String chnid) {
		this.chnid = chnid;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public int getKeyIndex() {
		return keyIndex;
	}
	public void setKeyIndex(int keyIndex) {
		this.keyIndex = keyIndex;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
