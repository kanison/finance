package com.zhaocb.zcb_app.bill.service.facade.dataobject;

import java.io.Serializable;

public class GenBillInput implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5225560014265615378L;

	public int listType;// ���� 0������10λ���� 101ת�˵��� 102���ֵ��� 103��ֵ����104���ᵥ�� 105�ⶳ����
	public int appId;// Ӧ��ID
	public String spId;// �̻���

	public int getListType() {
		return listType;
	}

	public void setListType(int listType) {
		this.listType = listType;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getSpId() {
		return spId;
	}

	public void setSpId(String spId) {
		this.spId = spId;
	}

}
