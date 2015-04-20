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
	 * c2c无密转账
	 * @param params
	 */
	public void noPwdC2cTransfer(C2CTransParams params);
	
	/**
	 * 解冻接口
	 * @param userAccountRollDO
	 * @param draw=true时解冻并提现，draw=false时直接解冻，不扣减账户余额
	 * @return
	 */
//	public UserAccountRollDO drawUserAccountUnfreeze(
//			UserAccountRollDO userAccountRollDO, boolean draw);

	/**
	 * 直接提现成功
	 * 
	 * @param userAccountRollDO
	 * @return
	 */
//	public UserAccountRollDO drawUserAccountDirectSuccess(
//			UserAccountRollDO userAccountRollDO);

	/**
	 * c2c转账
	 * 
	 * @param userAccountRollDO
	 */
//	public void userAccountTranfer(UserAccountRollDO fromUserAccountRollDO,
//			UserAccountRollDO toUserAccountRollDO);
}
