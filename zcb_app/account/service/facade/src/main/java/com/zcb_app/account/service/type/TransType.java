package com.zcb_app.account.service.type;
 
/**
 * @author Tinffy Lee
 * 交易相关字典数据
 */
public class TransType {
	
	//交易类型trans type
	public static final int TT_C2C_TRANS = 1; //c2c转账
	
	//记账类型bookkeeping type
	public static final int BKT_PAY_IN = 1; //入
	public static final int BKT_PAY_OUT = 2; //出
	public static final int BKT_NOT_IN_OUT = 3; //无出入
	
	//交易操作类型acction type
	public static final int ACT_C2C_TRSFR = 1; //C2C转账 c2c transfer
	public static final int ACT_C2C_TRSFR_FREEZE = 7; //C2C解冻并转账	c2c transfer unfreeze
	public static final int ACT_C2C_UNFREEZE_TRSFR = 8; //C2C解冻并转账	c2c unfreeze transfer
	public static final int ACT_C2C_UNFREEZE_TRSFR_FREEZE = 10; //C2C解冻并转账,冻结转账金额	
}
