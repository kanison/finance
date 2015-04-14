package com.zhaocb.zcb_app.finance.service.dao;

import com.zhaocb.zcb_app.finance.service.facade.dataobject.DeviceInfoDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.UserInfoDO;


public interface FundDAO  {
	//ͳ��װ����
	public long insertDeviceInfo(DeviceInfoDO deviceInfoDO);
	public DeviceInfoDO queryDeviceInfo(DeviceInfoDO deviceInfoDO);
	public void updateDeviceInfo(DeviceInfoDO deviceInfoDO);
	
	/**
	 * �û�ע����Ϣ����
	 * @param deviceInfoDO
	 * @return
	 */
	public long insertUserInfo(UserInfoDO userInfoDO);
	
	/**
	 * �û�ע����Ϣ��ѯ 
	 * @param userName
	 * @return
	 */
	public UserInfoDO queryUserByUserName(String userName);
}
