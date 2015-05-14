package com.zcb_app.sms.service.utils;


/**
 * @author Gu.Dongying
 */
public class SMSServiceCommonConstant {

	// 等号
	public static final String EQUALITY_SIGN = "=";
	// true字符串
	public static final String BOOLEAN_VAL_TRUE = "true";
	// 手机号正则表达式
	public static final String MOBILE_PHONE_REGEXP = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0,6-8])|(14[5,7]))\\d{8}$";//"^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
	// IP地址正则表达式
	public static final String IPV4_REGEX = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
	public static final String IPV6_STD_REGEX = "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$";
	public static final String IPV6_HEX_REGEX = "^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$";

	public static final String DOLLAR_FLAG = "$";
	public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static final int USE_BAK_PORT = 1;//使用备用渠道发送短信
}
