package com.zhaocb.common.signature.po;

import java.io.Serializable;

/**
 * �Ƹ�ͨ��Ӧ�̻�������Ϣ
 * 
 * @author aixxia
 * 
 */
public class TenpayResponeKeyPO implements Serializable{

	private static final long serialVersionUID = -5987254160857960953L;

	/**
	 * �̻����
	 */
	private String merchantID;

	/**
	 * �Ƹ�ͨ��Ӧ��Ϣ˽Կ
	 */
	private String responePrivateKey;

	/**
	 * �Ƹ�ͨ��Ӧ��Ϣ��Կ
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
