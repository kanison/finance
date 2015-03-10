package com.zhaocb.zcb_app.finance.service.facade;

import com.zhaocb.zcb_app.finance.service.facade.dataobject.AdvanceTypeConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpBizConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.TradeOrderDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.UserBindDO;

public interface FinanceFacade {

	public SpConfigDO querySpConfig(String spid, String bizCode);

	public SpBizConfigDO querySpBizConfig(String spid, String bizCode);

	public AdvanceTypeConfigDO queryAdvanceConfig(int advanceId); // 查询垫子信息

	public void createTradeOrder(TradeOrderDO TradeOrderDO); // 创建订单

	public UserBindDO queryUserBindInfo(UserBindDO userBindDO); // 查询用户绑定信息
}
