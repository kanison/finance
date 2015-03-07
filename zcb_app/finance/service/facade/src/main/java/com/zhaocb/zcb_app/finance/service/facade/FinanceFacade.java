package com.zhaocb.zcb_app.finance.service.facade;

import java.math.BigDecimal;

public interface FinanceFacade {
	public BigDecimal querySpCreditBalance(String spid,String bizCode);  // ��ѯ�̻��������ö��
	public void checkCreditBalance(String spid,String bizCode,BigDecimal totalMoney); // ������ö��
}
