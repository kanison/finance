package com.zcb_app.account.service.dao;

import java.util.List;

import com.zcb_app.account.service.dao.type.AcctTransParams;
import com.zcb_app.account.service.dao.type.SpUserInfo;
import com.zcb_app.account.service.facade.dataobject.TransVoucherDO;
import com.zcb_app.account.service.facade.dataobject.UserAccountDO;
import com.zcb_app.account.service.facade.dataobject.UserAccountRollDO;

public interface UserAccountDAO {
	public void createUserAccount(UserAccountDO userAccountDO);

	public UserAccountDO queryUserAccount(UserAccountDO userAccountDO);

	/**
	 * 查询商户信息
	 * @param obj
	 * @return
	 */
	public SpUserInfo querySpUser(String spid);
	
	//public UserAccountRollDO saveUserAccount(UserAccountRollDO userAccountRollDO);

	/**
	 * 查询交易凭证信息
	 * @param obj
	 * @return
	 */
	public TransVoucherDO queryTransVoucher(TransVoucherDO obj);
	
	public void c2cTransfer(AcctTransParams params);
	
	//public UserAccountRollDO drawUserAccountFreeze(
	//		UserAccountRollDO userAccountRollDO);
	
	

	/**
	 * 解冻接口
	 * 
	 * @param userAccountRollDO
	 * @param draw
	 *            =true时解冻并提现，draw=false时直接解冻，不扣减账户余额
	 * @return
	 */
	//public UserAccountRollDO drawUserAccountUnfreeze(
	//		UserAccountRollDO userAccountRollDO, boolean draw);

	/**
	 * 直接提现成功
	 * 
	 * @param userAccountRollDO
	 * @return
	 */
	//public UserAccountRollDO drawUserAccountDirectSuccess(
	//		UserAccountRollDO userAccountRollDO);

	/**
	 * c2c转账
	 * 
	 * @param userAccountRollDO
	 */
	//public void userAccountTranfer(UserAccountRollDO fromUserAccountRollDO,
	//		UserAccountRollDO toUserAccountRollDO);

	//public List<UserAccountRollDO> queryUserAccountRolllist(
	//		UserAccountRollDO userAccountRollDO);
}
