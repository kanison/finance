package com.zhaocb.zcb_app.bill.service.facade.dataobject;

import java.io.Serializable;

public class AppIdInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 28832244769782231L;

	public String appId;
	public int startNo;// 开始自增号

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public int getStartNo() {
		return startNo;
	}

	public void setStartNo(int startNo) {
		this.startNo = startNo;
	}

}
