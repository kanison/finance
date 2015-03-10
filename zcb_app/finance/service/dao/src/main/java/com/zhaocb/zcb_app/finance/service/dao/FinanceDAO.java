package com.zhaocb.zcb_app.finance.service.dao;


import com.zhaocb.zcb_app.finance.service.facade.dataobject.AdvanceTypeConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpBizConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.TradeOrderDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.UserBindDO;

public interface FinanceDAO  {
	public SpConfigDO querySpConfig(SpConfigDO spConfigDO);
	
	public SpBizConfigDO querySpBizConfig(SpBizConfigDO spBizConfigDO);
	
	public AdvanceTypeConfigDO queryAdvanceTypeConfig(AdvanceTypeConfigDO advanceTypeConfigDO);
	
	public void insertTradeOrder(TradeOrderDO tradeOrderDO);
	
	public UserBindDO queryUserBindInfo(UserBindDO userBindDO);
}
