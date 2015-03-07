package com.zhaocb.zcb_app.finance.service.facade;

import java.math.BigDecimal;

public interface FinanceFacade {
	public BigDecimal querySpCreditBalance(String spid,String bizCode);  // 查询商户可用信用额度
	public void checkCreditBalance(String spid,String bizCode,BigDecimal totalMoney); // 检查信用额度
}
