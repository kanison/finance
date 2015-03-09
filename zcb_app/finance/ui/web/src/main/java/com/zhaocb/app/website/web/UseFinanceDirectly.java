package com.zhaocb.app.website.web;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.app.aop.annotation.LogMethod;
import com.zhaocb.app.website.web.model.UseFinanceInput;
import com.zhaocb.app.website.web.model.UseFinanceOutput;
import com.zhaocb.app.website.web.util.FinanceWebComm;
import com.zhaocb.zcb_app.finance.service.facade.FinanceFacade;
import com.zhaocb.zcb_app.finance.service.facade.FundFacade;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpConfigDO;


@RequestMapping
public class UseFinanceDirectly {
	private Map<String, String> appConfig;
	private FundFacade fundFacade;
	private FinanceFacade financeFacade;
	private static final Log LOG = LogFactory.getLog(UseFinanceInput.class);

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	@LogMethod
	public UseFinanceOutput handleTestFinance(UseFinanceInput useFinanceInput)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		
		// 检查参数
		checkParam(useFinanceInput);
		// 检查额度是否可用
		SpConfigDO spConfigDO = financeFacade.querySpConfig(useFinanceInput.getSpid(), useFinanceInput.getBizCode());
		FinanceWebComm.checkCreditBalance(spConfigDO, useFinanceInput.getTotalFee());  // 不足直接抛出异常
		// 记录订单
		
		// 垫资账户c2c转账给提现
		
		// 扣减商户可用额度
		
		// 返回
		UseFinanceOutput useFinanceOutput = new UseFinanceOutput();
		return useFinanceOutput;
	}
	
	
		
	
	
	
	public void checkParam(UseFinanceInput useFinanceInput){
		
	}
	
	public FundFacade getFundFacade() {
		return fundFacade;
	}

	public void setFundFacade(FundFacade fundFacade) {
		this.fundFacade = fundFacade;
	}

	public FinanceFacade getFinanceFacade() {
		return financeFacade;
	}

	public void setFinanceFacade(FinanceFacade financeFacade) {
		this.financeFacade = financeFacade;
	}
	
	public Map<String, String> getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(Map<String, String> appConfig) {
		this.appConfig = appConfig;
	}
}

