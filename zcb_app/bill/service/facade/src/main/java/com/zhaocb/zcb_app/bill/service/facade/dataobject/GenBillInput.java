package com.zhaocb.zcb_app.bill.service.facade.dataobject;

import java.io.Serializable;

public class GenBillInput implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5225560014265615378L;

	public String listType;// ���� 0������10λ���� 101ת�˵��� 102���ֵ��� 103��ֵ����104���ᵥ�� 105�ⶳ����
	public String appId;// Ӧ��ID
	public String spId;// �̻���
	public AppIdInfo appIdInfo;

	public AppIdInfo getAppIdInfo() {
		return appIdInfo;
	}

	public void setAppIdInfo(AppIdInfo appIdInfo) {
		this.appIdInfo = appIdInfo;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSpId() {
		return spId;
	}

	public void setSpId(String spId) {
		this.spId = spId;
	}

}
