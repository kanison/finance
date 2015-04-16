package com.zhaocb.zcb_app.bill.service.dao;

import com.zhaocb.zcb_app.bill.service.facade.dataobject.AppIdInfo;

public interface BillDAO {

	public AppIdInfo queryAppIdInfo(String appId);
	
}
