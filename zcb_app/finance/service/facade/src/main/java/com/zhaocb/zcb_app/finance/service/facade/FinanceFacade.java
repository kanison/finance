package com.zhaocb.zcb_app.finance.service.facade;

import com.zcb_app.account.service.facade.dataobject.UserAccountRollDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.AdvanceTypeConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpBizConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.TradeOrderDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.UserBindDO;

public interface FinanceFacade {

	/**
	 * ��ѯ�̻���Ϣ
	 * @param spid
	 * @param bizCode
	 * @return
	 */
	public SpConfigDO querySpConfig(String spid, String bizCode);

	/**
	 * ��ѯ�̻�ҵ����Ϣ
	 * @param spid
	 * @param bizCode
	 * @return
	 */
	public SpBizConfigDO querySpBizConfig(String spid, String bizCode);

	/**
	 * ��ѯ������Ϣ
	 * @param advanceId
	 * @return
	 */
	public AdvanceTypeConfigDO queryAdvanceConfig(int advanceId); 

	/**
	 * ��������
	 * @param TradeOrderDO
	 */
	public void createTradeOrder(TradeOrderDO TradeOrderDO); 
	
	/**
	 * ��ѯ�û�����Ϣ
	 * @param userBindDO
	 * @return
	 */
	public UserBindDO queryUserBindInfo(UserBindDO userBindDO); 
	
	/**
	 * c2cֱ��ת��
	 * @param fromUser
	 * @param toUser
	 */
	public void c2cTransferDirectly(UserAccountRollDO fromUser,UserAccountRollDO toUser); 
	
	/**
	 * ���̻�
	 * @param userBindDO
	 */
	public void bindUser(UserBindDO userBindDO);
	
	/**
	 * ���¶�����Ϣ
	 * @param tradeOrderDO
	 */
	public void updateTradeOrder(TradeOrderDO tradeOrderDO);
}
