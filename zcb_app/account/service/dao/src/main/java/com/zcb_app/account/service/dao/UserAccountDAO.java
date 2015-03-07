package com.zcb_app.account.service.dao;

import com.zcb_app.account.service.facade.dataobject.UserAccountDO;
import com.zcb_app.account.service.facade.dataobject.UserAccountRollDO;

public interface UserAccountDAO {
	public void createUserAccount(UserAccountDO userAccountDO);

	public UserAccountDO queryUserAccount(UserAccountDO userAccountDO);

	public UserAccountRollDO saveUserAccount(UserAccountRollDO userAccountRollDO);

	public UserAccountRollDO drawUserAccountFreeze(
			UserAccountRollDO userAccountRollDO);

	public UserAccountRollDO drawUserAccountUnfreeze(
			UserAccountRollDO userAccountRollDO, boolean success);

	/**
	 * 直接提现成功
	 * 
	 * @param userAccountRollDO
	 * @return
	 */
	public UserAccountRollDO drawUserAccountDirectSuccess(
			UserAccountRollDO userAccountRollDO);

	/**
	 * c2c转账
	 * 
	 * @param userAccountRollDO
	 */
	public void userAccountTranfer(UserAccountRollDO userAccountRollDO);
}
