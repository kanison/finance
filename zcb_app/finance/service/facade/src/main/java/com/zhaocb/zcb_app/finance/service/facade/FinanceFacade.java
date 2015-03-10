package com.zhaocb.zcb_app.finance.service.facade;

import com.zhaocb.zcb_app.finance.service.facade.dataobject.AdvanceTypeConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpBizConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.TradeOrderDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.UserBindDO;

public interface FinanceFacade {

	public SpConfigDO querySpConfig(String spid, String bizCode);

	public SpBizConfigDO querySpBizConfig(String spid, String bizCode);

	public AdvanceTypeConfigDO queryAdvanceConfig(int advanceId); // ��ѯ������Ϣ

	public void createTradeOrder(TradeOrderDO TradeOrderDO); // ��������

	public UserBindDO queryUserBindInfo(UserBindDO userBindDO); // ��ѯ�û�����Ϣ
}
