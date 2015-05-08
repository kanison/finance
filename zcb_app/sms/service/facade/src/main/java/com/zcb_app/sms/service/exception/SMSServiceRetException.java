package com.zcb_app.sms.service.exception;

import com.tenpay.sm.lang.error.ErrorCodeException;

public class SMSServiceRetException  extends ErrorCodeException{
	
	private static final long serialVersionUID = 954043913218832333L;
	
	public static final String SYSTEM_ERROR="30010000";//系统错误
	
	public static final String ERR_MOBILE_PARAMS = "30010001";//手机号输入错误
	
	public static final String ERR_TEMPLATE_PARAMS = "30010002";//短信模板错误
	
	public static final String ERR_CLIENT_IP_PARAMS = "30010003";//用户IP错误
	
	public static final String ERR_VERIFY_CODE_PARAMS = "30010004";//验证码错误
	
	public static final String ERR_OPERATION_CODE_PARAMS = "30010005";//发送通知消息操作码错误
	
	public static final String ERR_SEND_CODE_PARAMS = "30010006";//下发短信验证码参数错误!
	
	public static final String ERR_IP_INTERNAL_AUTHORIZATION = "30010007";//客户端IP非部授权IP!
	
	public static final String ERR_VERIFY_CODE = "30010008";//验证码不匹配！
	
	public static final String ERR_VERIFY_CODE_EXPIRED = "30010010";//验证码已过期！
	
	public static final String ERR_VERIFY_CODE_TIMES = "30010011";//验证码校验超过校验次数！
	
	public static final String ERR_EFFECTED_ROWS = "30010009";//更新的影响行数不为1
	
	public static final String ERR_EXCEED_MOBILE_NO_LIMIT = "300100012";//手机号超过频率限制
	
	public static final String ERR_EXCEED_IP_LIMIT = "300100013";//IP地址超过频率限制
	
	public SMSServiceRetException(String msg) {
		super(msg);
	}

	public SMSServiceRetException(String msg, Exception e) {
		super(msg, e);
	}

	public SMSServiceRetException(Exception e) {
		super(e);
	}
	
	public SMSServiceRetException(String retcode, String retmsg) {
		
		super(retcode, retmsg);
	}
}
