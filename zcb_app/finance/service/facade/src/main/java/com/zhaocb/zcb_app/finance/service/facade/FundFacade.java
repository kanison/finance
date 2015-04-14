package com.zhaocb.zcb_app.finance.service.facade;

import com.zhaocb.zcb_app.finance.service.facade.dataobject.DeviceInfoDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.UserInfoDO;



public interface FundFacade {


	public long insertDeviceInfo(DeviceInfoDO deviceInfoDO);
	public DeviceInfoDO queryDeviceInfo(DeviceInfoDO deviceInfoDO);
	public void updateDeviceInfo(DeviceInfoDO deviceInfoDO);
	
	/**
	 * 用户注册信息保存
	 * @param deviceInfoDO
	 * @return
	 */
	public long insertUserInfo(UserInfoDO userInfoDO);
	
	/**
	 * 用户注册信息查询 
	 * @param userName
	 * @return
	 */
	public UserInfoDO queryUserByUserName(String userName);
}
