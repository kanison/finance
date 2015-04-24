package com.zhaocb.common.signature.po;

import java.io.Serializable;

/**
 * �̻�����Ƹ�ͨ�Ľ���������Ϣ
 * 
 * @author aixxia
 * 
 */
public class MerchantRequestKeyPO implements Serializable{


	private static final long serialVersionUID = 8149214207061867640L;

	/**
	 * �̻����
	 */
	private String merchantID;

	/**
	 * �̻�������Ϣ��Կ
	 */
	private String requestPublicKey;

	/**
	 * �̻�������Ϣ��Կ���
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
