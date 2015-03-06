package com.zhaocb.zcb_app.finance.service.dao;

import com.zhaocb.zcb_app.finance.service.facade.dataobject.DeviceInfoDO;


public interface FundDAO  {
	//统计装机量
	public long insertDeviceInfo(DeviceInfoDO deviceInfoDO);
	public DeviceInfoDO queryDeviceInfo(DeviceInfoDO deviceInfoDO);
	public void updateDeviceInfo(DeviceInfoDO deviceInfoDO);
}
