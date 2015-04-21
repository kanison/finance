package com.zcb_app.account.service.facade;

import com.zcb_app.account.service.facade.dataobject.C2CTransParams;
import com.zcb_app.account.service.facade.dataobject.UserAccountDO;
import com.zcb_app.account.service.facade.dataobject.UserAccountRollDO;



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
