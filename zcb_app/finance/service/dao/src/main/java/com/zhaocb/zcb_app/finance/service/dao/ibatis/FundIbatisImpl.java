/**
 * 
 */
package com.zhaocb.zcb_app.finance.service.dao.ibatis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.zhaocb.zcb_app.finance.service.dao.FundDAO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.DeviceInfoDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.UserInfoDO;

public class FundIbatisImpl extends SqlMapClientDaoSupport implements FundDAO {
	private static final Log LOG = LogFactory.getLog(FundIbatisImpl.class);

	//统计装机量
	public long insertDeviceInfo(DeviceInfoDO deviceInfoDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (Long)client.insert("insertDeviceInfo", deviceInfoDO);
	}

	public DeviceInfoDO queryDeviceInfo(DeviceInfoDO deviceInfoDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (DeviceInfoDO) client.queryForObject("queryDeviceInfo", deviceInfoDO);
	}

	public void updateDeviceInfo(DeviceInfoDO deviceInfoDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.update("updateDeviceInfo", deviceInfoDO);
	}

	public long insertUserInfo(UserInfoDO userInfoDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (Long)client.insert("insertUserInfo", userInfoDO);
	}

	public UserInfoDO queryUserByUserName(String userName) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (UserInfoDO) client.queryForObject("queryUserByUserName", userName);
	}

}
