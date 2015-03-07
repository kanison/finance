package com.zhaocb.zcb_app.finance.service.finance;

import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import com.app.utils.AliSpyMemCachedWrapper;
import com.zhaocb.zcb_app.finance.service.dao.FundDAO;
import com.zhaocb.zcb_app.finance.service.facade.FundFacade;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.DeviceInfoDO;

public class FundFacadeImpl implements FundFacade {
	private static final Log LOG = LogFactory.getLog(FundFacadeImpl.class);
	private FundDAO fundDAO;

	private Random random = new Random();
	private Map<String, String> appConfig;
	private AliSpyMemCachedWrapper aliSpyMemCache;

	public FundDAO getFundDAO() {
		return fundDAO;
	}

	public void setFundDAO(FundDAO fundDAO) {
		this.fundDAO = fundDAO;
	}

	public Map<String, String> getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(Map<String, String> appConfig) {
		this.appConfig = appConfig;
	}

	public void setAliSpyMemCache(AliSpyMemCachedWrapper aliSpyMemCache) {
		this.aliSpyMemCache = aliSpyMemCache;
	}

	public AliSpyMemCachedWrapper getAliSpyMemCache() {
		return aliSpyMemCache;
	}

	public long insertDeviceInfo(DeviceInfoDO deviceInfoDO) {
		return fundDAO.insertDeviceInfo(deviceInfoDO);
	}

	public DeviceInfoDO queryDeviceInfo(DeviceInfoDO deviceInfoDO) {
		return fundDAO.queryDeviceInfo(deviceInfoDO);
	}

	@Transactional
	public void updateDeviceInfo(DeviceInfoDO deviceInfoDO) {
		fundDAO.updateDeviceInfo(deviceInfoDO);
	}

}


