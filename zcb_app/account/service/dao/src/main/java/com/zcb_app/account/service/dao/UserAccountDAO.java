package com.zcb_app.account.service.dao;

import com.zcb_app.account.service.dao.type.AcctFreezeBalanParams;
import com.zcb_app.account.service.dao.type.AcctTransParams;
import com.zcb_app.account.service.dao.type.AcctUnFreezeBalanceParams;
import com.zcb_app.account.service.dao.type.SpUserInfo;
import com.zcb_app.account.service.facade.dataobject.TransVoucherDO;
import com.zcb_app.account.service.facade.dataobject.UserAccountDO;

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

	/**
	 * 操作冻结金额<br>
	 * 1、	查询加锁用户交易账户<br>
	 * 2、	判断用户的可用金额(非冻结余额)是否足够，不够报余额不足错误<br>
	 * 3、	增加冻结金额<br>
	 * 4、	记录冻结单<br>
	 * 5、	记录交易凭证流水
	 * @param params
	 * @author Gu.Dongying 
	 * @date 2015年4月24日 上午11:49:08
	 */
	public void freezeUserBalance(AcctFreezeBalanParams params);

	/**
	 * 解冻金额接口<br>
	 * 1、 查询加锁用户交易账户<br>
	 * 2、 判断用户的冻结金额是否足够，不够报余额不足错误<br>
	 * 3、 查询冻结单信息，并校验状态和金额是否正确<br>
	 * 4、 修改冻结单的解冻金额和状态<br>
	 * 5、 记录用户账户流水<br>
	 * 6、 记录交易凭证流水
	 * @param unFreeze
	 * @Return void
	 * @author Gu.Dongying 
	 * @Date 2015年4月27日 上午11:41:13
	 */
	public void unfreezeUserBalance(AcctUnFreezeBalanceParams unFreeze);
	
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
