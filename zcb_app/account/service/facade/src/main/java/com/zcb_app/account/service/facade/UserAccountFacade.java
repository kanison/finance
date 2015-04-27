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
	 * c2c无密转账
	 * @param params
	 */
	public void noPwdC2cTransfer(C2CTransParams params);
	
	/**
	 * 冻结接口
	 * @param params
	 * @return 返回结果
	 * @Return String
	 * @author Gu.Dongying 
	 * @Date 2015年4月24日 上午9:59:59
	 */
	public String freezeUserBalance(FreezeBalanceParams params);
	
	/**
	 * 解冻接口
	 * @param params
	 * @return 返回解冻结果
	 * @Return String
	 * @author Gu.Dongying 
	 * @Date 2015年4月27日 上午10:34:13
	 */
	public String unfreezeUserBalance(UnFreezeBalanceParams params);
	
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
