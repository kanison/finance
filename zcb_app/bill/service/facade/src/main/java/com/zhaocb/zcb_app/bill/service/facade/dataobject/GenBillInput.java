package com.zhaocb.zcb_app.bill.service.facade.dataobject;

import java.io.Serializable;

public class GenBillInput implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5225560014265615378L;

	public String listType;// 类型 0仅生成10位单号 101转账单号 102提现单号 103充值单号104冻结单号 105解冻单号
	public String appId;// 应用ID
	public String spId;// 商户号
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
