package com.zhaocb.zcb_app.finance.service.finance;

import java.math.BigDecimal;
import java.util.Map;

import com.zhaocb.zcb_app.finance.service.facade.FinanceFacade;

public class FinanceFacadeImp implements FinanceFacade {
	private Map<String, String> appConfig;
	public BigDecimal querySpCreditBalance(String spid,String bizCode){  // 查询商户可用信用额度
		
		return new BigDecimal(0);
	}
	public void checkCreditBalance(String spid,String bizCode,BigDecimal totalMoney){
		BigDecimal creditBalance = querySpCreditBalance(spid,bizCode);
		
	}
	
	public void querySpinfo(String spid,String bizCode){
		return ;
	}
	public Map<String, String> getAppConfig() {
		return appConfig;
	}
	public void setAppConfig(Map<String, String> appConfig) {
		this.appConfig = appConfig;
	}
	
}
