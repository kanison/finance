package com.zhaocb.zcb_app.finance.service.facade;

import com.zcb_app.account.service.facade.dataobject.UserAccountRollDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.AdvanceTypeConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpBizConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.TradeOrderDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.UserBindDO;

public interface FinanceFacade {

	/**
	 * 查询商户信息
	 * @param spid
	 * @param bizCode
	 * @return
	 */
	public SpConfigDO querySpConfig(String spid, String bizCode);

	/**
	 * 查询商户业务信息
	 * @param spid
	 * @param bizCode
	 * @return
	 */
	public SpBizConfigDO querySpBizConfig(String spid, String bizCode);

	/**
	 * 查询垫子信息
	 * @param advanceId
	 * @return
	 */
	public AdvanceTypeConfigDO queryAdvanceConfig(int advanceId); 

	/**
	 * 创建订单
	 * @param TradeOrderDO
	 */
	public void createTradeOrder(TradeOrderDO TradeOrderDO); 
	
	/**
	 * 查询用户绑定信息
	 * @param userBindDO
	 * @return
	 */
	public UserBindDO queryUserBindInfo(UserBindDO userBindDO); 
	
	/**
	 * c2c直接转账
	 * @param fromUser
	 * @param toUser
	 */
	public void c2cTransferDirectly(UserAccountRollDO fromUser,UserAccountRollDO toUser); 
	
	/**
	 * 绑定商户
	 * @param userBindDO
	 */
	public void bindUser(UserBindDO userBindDO);
	
	/**
	 * 更新订单信息
	 * @param tradeOrderDO
	 */
	public void updateTradeOrder(TradeOrderDO tradeOrderDO);
}
