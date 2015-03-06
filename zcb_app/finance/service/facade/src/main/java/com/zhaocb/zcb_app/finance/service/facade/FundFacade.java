package com.zhaocb.zcb_app.finance.service.facade;

import com.zhaocb.zcb_app.finance.service.facade.dataobject.DeviceInfoDO;



public interface FundFacade {


	public long insertDeviceInfo(DeviceInfoDO deviceInfoDO);
	public DeviceInfoDO queryDeviceInfo(DeviceInfoDO deviceInfoDO);
	public void updateDeviceInfo(DeviceInfoDO deviceInfoDO);
}
