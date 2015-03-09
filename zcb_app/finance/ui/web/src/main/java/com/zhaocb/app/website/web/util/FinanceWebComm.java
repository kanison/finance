package com.zhaocb.app.website.web.util;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zhaocb.app.website.web.exception.WebServiceRetException;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.AdvanceTypeConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpConfigDO;

public class FinanceWebComm {
	private static Log LOG = LogFactory.getLog(FinanceWebComm.class);
	/**
	 *  检查信用额度
	 *  系统首先使用的是自有额度，自有额度使用完后使用垫资额度，所以这里只需要检查垫资额度是否可用
	 * @param spConfigDO
	 * @param totalMoney
	 */
	public static void checkCreditBalance(SpConfigDO spConfigDO,BigDecimal totalMoney){
		if(spConfigDO == null || spConfigDO.getAdvanceTypeConfigDo() == null){
			LOG.info("查询商户信息有误，查询结果spConfigDO 或者 spConfigDO 为空");
			throw new WebServiceRetException(WebServiceRetException.UNEXPECTED_ERR,"系统繁忙，请稍候再试");
		}
		AdvanceTypeConfigDO advanceTypeDO = spConfigDO.getAdvanceTypeConfigDo();
		BigDecimal canUseMoney = advanceTypeDO.getCreditQuoto().divide(advanceTypeDO.getUsedCreditQuoto()).divide(advanceTypeDO.getCreditQuotoOffset());
		if(canUseMoney.compareTo(totalMoney) < 0){
			LOG.info("商户可用信用额度不足，剩余额度 canUseMoney="+canUseMoney);
			throw new WebServiceRetException(WebServiceRetException.APP_CREDIT_QUOTO_NOT_ENOUGH,"商户信用额度不足");
		}
	}

	
}