package com.zcb_app.account.service.type;

/**
 * 冻结单的相关的类型字典值
 * @author Tinffy Lee
 *
 */
public class FreezeType {
	//冻结原因
	public static final int FR_TRANS_FREEZE = 1; //交易冻结
	public static final int FR_CSO_FREEZE = 2; //客服（Customer Service officer）冻结
	public static final int FR_RISK_FREEZE = 3; //风控冻结
	public static final int FR_FETCH_FREEZE = 4; //提现冻结
	
	//冻结单状态
	public static final int FS_INIT = 1; //初始
	public static final int FS_FROZEN = 2; //冻结
	public static final int FS_PARTLY_UFREEZE = 3; //部分解冻partly unfreeze
	public static final int FS_ALL_UFREEZE = 4; //全部解冻 all unfreeze
}
