package com.zhaocb.common.signature.po;

import java.io.Serializable;

/**
 * 财付通响应商户配置信息
 * 
 * @author aixxia
 * 
 */
public class TenpayResponeKeyPO implements Serializable{

	private static final long serialVersionUID = -5987254160857960953L;

	/**
	 * 商户编号
	 */
	private String merchantID;

	/**
	 * 财付通响应消息私钥
	 */
	private String responePrivateKey;

	/**
	 * 财付通响应消息公钥
	 */
	private String responePublicKey;

	public String getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getResponePrivateKey() {
		return responePrivateKey;
	}

	public void setResponePrivateKey(String responePrivateKey) {
		this.responePrivateKey = responePrivateKey;
	}

	public String getResponePublicKey() {
		return responePublicKey;
	}

	public void setResponePublicKey(String responePublicKey) {
		this.responePublicKey = responePublicKey;
	}

}
