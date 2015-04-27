package com.zcb_app.account.service.facade;

import com.zcb_app.account.service.facade.dataobject.C2CTransParams;
import com.zcb_app.account.service.facade.dataobject.FreezeBalanceParams;
import com.zcb_app.account.service.facade.dataobject.UnFreezeBalanceParams;
import com.zcb_app.account.service.facade.dataobject.UserAccountDO;



public interface UserAccountFacade {
	public void createUserAccount(UserAccountDO userAccountDO);

	public UserAccountDO queryUserAccount(UserAccountDO userAccountDO);

//	public UserAccountRollDO saveUserAccount(UserAccountRollDO userAccountRollDO);

//	public UserAccountRollDO drawUserAccountFreeze(
//			UserAccountRollDO userAccountRollDO);
	
	/**
	 * c2c����ת��
	 * @param params
	 */
	public void noPwdC2cTransfer(C2CTransParams params);
	
	/**
	 * ����ӿ�
	 * @param params
	 * @return ���ؽ��
	 * @Return String
	 * @author Gu.Dongying 
	 * @Date 2015��4��24�� ����9:59:59
	 */
	public String freezeUserBalance(FreezeBalanceParams params);
	
	/**
	 * �ⶳ�ӿ�
	 * @param params
	 * @return ���ؽⶳ���
	 * @Return String
	 * @author Gu.Dongying 
	 * @Date 2015��4��27�� ����10:34:13
	 */
	public String unfreezeUserBalance(UnFreezeBalanceParams params);
	
	/**
	 * �ⶳ�ӿ�
	 * @param userAccountRollDO
	 * @param draw=trueʱ�ⶳ�����֣�draw=falseʱֱ�ӽⶳ�����ۼ��˻����
	 * @return
	 */
//	public UserAccountRollDO drawUserAccountUnfreeze(
//			UserAccountRollDO userAccountRollDO, boolean draw);

	/**
	 * ֱ�����ֳɹ�
	 * 
	 * @param userAccountRollDO
	 * @return
	 */
//	public UserAccountRollDO drawUserAccountDirectSuccess(
//			UserAccountRollDO userAccountRollDO);

	/**
	 * c2cת��
	 * 
	 * @param userAccountRollDO
	 */
//	public void userAccountTranfer(UserAccountRollDO fromUserAccountRollDO,
//			UserAccountRollDO toUserAccountRollDO);
}
