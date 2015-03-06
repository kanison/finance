package com.zhaocb.app.website.web;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zhaocb.app.website.web.model.CommonOutput;
import com.zhaocb.zcb_app.finance.service.facade.FundFacade;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.DeviceInfoDO;

@RequestMapping
public class TestFinance {

	private FundFacade fundFacade;
	private static final Log LOG = LogFactory.getLog(TestFinance.class);

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	//@LogMethod
	public CommonOutput handleTestFinance(DeviceInfoDO deviceInfoDO)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		
		LOG.info("handleTextFinance imei = " + deviceInfoDO.getImei());
		
		DeviceInfoDO result = fundFacade.queryDeviceInfo(deviceInfoDO);
		LOG.info("result mac="+result.getMac());
		CommonOutput commonOutput = new CommonOutput();
		commonOutput.setRetCode(1);
		commonOutput.setRetMsg("测试休闲平台服务器搭建情况，2015-01-04");

		if (null != result) {
			commonOutput.setRetMsg(commonOutput.getRetMsg() + "    :imei = " + result.getImei()
					+ ", mac = " + result.getMac());
		}
		
		return commonOutput;
	}
	
	public FundFacade getFundFacade() {
		return fundFacade;
	}

	public void setFundFacade(FundFacade fundFacade) {
		this.fundFacade = fundFacade;
	}
	
}
