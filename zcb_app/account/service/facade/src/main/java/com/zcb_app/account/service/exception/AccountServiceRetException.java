package com.zcb_app.account.service.exception;

import com.tenpay.sm.lang.error.ErrorCodeException;


public class AccountServiceRetException  extends ErrorCodeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 954043913218832333L;
	/**
	 * 错误码前3位是模块名，本模块为200；
	 * 第四位1表示系统错误，2表示业务错误；
	 * 后四位为具体错误
	 */
	public static final String SYSTEM_ERROR="20010000";//系统错误
	public static final String PARSE_PARAM_ERROR="20010001";//解析参数异常
	public static final String ERR_DATA_SIGN_ABN = "20010002";//数据签名异常
	public static final String ERR_EFFECTED_ROWS = "20010003";//更新的影响行数不为1
	
	//业务参数，20029001~20029999为统一的业务错误码
	public static final String INPUT_PARAMS_ERROR="20029001";//参数错误
	public static final String ERR_REENTY_INCONSISTENT = "20029002";//重入参数不一致
	public static final String ERR_REENTY_OK = "20029003";//重入错误码，参数一致
	
	public static final String USER_EXIST="20020001";//用户已存在
	public static final String USER_NOT_EXIST="20020002";//用户未注册
	public static final String PASSWORD_ERROR="20020003";//密码错误
	public static final String BALANCE_NOT_ENOUGH="20020004";//账户余额不足
	public static final String FREEZE_BALANCE_NOT_ENOUGH="20020005";//账户冻结余额不足
	public static final String LISTID_NOT_EXIST="20020006";//单号不存在
	public static final String LISTID_FREEZE_ERROR="20020007";//冻结单错误
	public static final String FREEZE_ID_ERROR="20020008";//解冻单号错误
	public static final String FREEZE_BALANCE_ERROR="2002009";//解冻金额错误
	public static final String FREEZE_STATUS_ERROR="2002010";//原冻结单状态错误
		
	public static final String ERR_USER_NOT_EXSIT = "20020010";//用户不存在
	public static final String ERR_USER_ACCT_NOT_EXSIT = "20020011";//用户现金账号不存在
	
	public AccountServiceRetException(String msg) {
		super(msg);
	}

	public AccountServiceRetException(String msg, Exception e) {
		super(msg, e);
	}

	public AccountServiceRetException(Exception e) {
		super(e);
	}
	
	public AccountServiceRetException(String retcode, String retmsg) {
		
		super(retcode, retmsg);
	}
}
