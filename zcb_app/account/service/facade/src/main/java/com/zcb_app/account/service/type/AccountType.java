package com.zcb_app.account.service.type;

/**
 * 用户现金账户相关字典数据
 * @author Tinffy Lee
 *
 */
public class AccountType {
	//账户类型user account type
	public static final int UAT_NORMAL = 1; //普通用户账户
	public static final int UAT_SP = 2; //商户账户
	public static final int UAT_BANK = 3; //银行账户
	
	//账户状态user account state
	public static final int UAS_NORMAL = 1; //正常状态
	public static final int UAS_FROZEN = 2; //冻结状态
}
