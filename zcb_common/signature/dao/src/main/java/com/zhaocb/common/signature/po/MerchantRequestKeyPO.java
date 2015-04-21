package com.zhaocb.common.signature.po;

import java.io.Serializable;

/**
 * 商户接入财付通的交互配置信息
 * 
 * @author aixxia
 * 
 */
public class MerchantRequestKeyPO implements Serializable{


	private static final long serialVersionUID = 8149214207061867640L;

	/**
	 * 商户编号
	 */
	private String merchantID;

	/**
	 * 商户请求消息公钥
	 */
	private String requestPublicKey;

	/**
	 * 商户请求消息公钥序号
	 */
	private int requestKeyIndex;

	public String getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getRequestPublicKey() {
		return requestPublicKey;
	}

	public void setRequestPublicKey(String requestPublicKey) {
		this.requestPublicKey = requestPublicKey;
	}

	public int getRequestKeyIndex() {
		return requestKeyIndex;
	}

	public void setRequestKeyIndex(int requestKeyIndex) {
		this.requestKeyIndex = requestKeyIndex;
	}

}
