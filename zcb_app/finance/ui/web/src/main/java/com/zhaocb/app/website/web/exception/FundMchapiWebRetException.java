package com.zhaocb.app.website.web.exception;

import com.tenpay.sm.lang.error.ErrorCodeException;

public class FundMchapiWebRetException  extends ErrorCodeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4660797621140148970L;
	public static final String SYSTEM_ERROR="87710000";//系统错误
	public static final String FILE_NOT_EXIST ="87720303";//文件不存在
	
	
	
	public static final String PARSE_PARAM_ERROR="87710001";//解析参数异常
	
	public static final String NO_OR_INCORRECT_ID_CARD="87720202";//非身份证类型暂不支持开通基金易，如有问题与财付通客服联系
	public static final String NO_FUND_ACCOUNT="87720203";//未开通基理财账户
	public static final String ERR_UNKOWN_FUNDCOR="87720204";//指定基金公司尚未和财付通签署协议
	public static final String EXIST_CONTRACT_FUNDCOR="87720205";//已经与该基金公司签约
	public static final String CALL_PL_PAY_EXCEPTION="87720206";//后台支付cgi返回-1的转义处理
	public static final String LOGIN_ERROR="87720200";//登录异常
	public static final String NOT_TRUE_NAME_USER="87720207";//实名检查未通过
	public static final String VERIFY_CODE_ERROR="87720208";//手机验证码不正确
	public static final String NO_FUND_MOBILE="87720209";  //没有查询到基金账户绑定的手机号码
	public static final String NO_BAND_CARD="87720210";  //没有绑定银行卡
	public static final String ERR_CREID="87720211";  //身份证信息验证出错
	public static final String ERR_OLD_MOBILE="87720212";  //原手机号码验证出错
	public static final String SEND_PACK_MOBILE_ERROR="87720213";  //发送手机验证码组包出错
	public static final String NO_FUND_DRAW_LIST="87720214";  //没有提现记录
	public static final String NO_FUND_ACCOUNT_INFO="87720215";  //没有查到基金用户账户信息  余额等信息
	public static final String NO_FUND_ACCOUNT_DETAIL="87720216";  //没有账户明细信息
	public static final String NO_SUPPORT_FUND_CORP="87720217";  //没有账户明细信息
	public static final String ERR_SUPPORT_FUND_CORP="87720218";  //基金公司信息不完整
	public static final String UNKONWN_SENDSCENE="87720219";  //未知发送短信验证码场景
	public static final String NO_TRANS_LIST="87720220";  //没有交易记录
	public static final String ERR_MODIFY_MOBILE="87720221";  //更换手机号出错
	public static final String ERR_SAME_MOBILE="87720222";  //该手机已经与您的财付通绑定,请输入新手机号码
	public static final String ERR_REP_MBCODE_NOTIME ="87720223"; //验证码发送频繁，请稍后重新点击发送
	public static final String USER_NOT_AGREE_SHARE="87720224";  //用户未同意财付通从基金公司获取持仓信息
	public static final String ERR_MOBILE_VERIFIED_EXPR_TIME="87720225";  //请重新验证旧手机号码
	public static final String MOBILE_USER_CANNOT_MODIFY="87720226";  //手机号的财付通用户不允许修改手机号码
	public static final String USER_TYPE_ERROR="87720227";  //用户类型错误
	public static final String PAY_PASSWD_ERROR="87720228";  //密码错误，请注意数字字母组合及大小写。
	public static final String NOT_REGISTER_CFT="87720229";  //没有激活财付通
	
	public static final String ERR_ACCOUNT_INFO="87720009";//请求信息与基金易帐户信息不一致
	public static final String ERR_OPEN_FUND_ACCOUNT="87720008";//已经开通基金交易帐户
	public static final String ERR_CRE_OPEN_FUND_ACCOUNT="87720010";//该身份证件用户已经开通基金交易帐户
	public static final String ERR_EXIST_BIND_CARD ="20021"; //已绑定过银行帐号，不能再次绑定
	
	public static final String CAN_NOT_UPLOAD_FILE ="87720300"; //上传次数过多或已超过文件可上传时间
	public static final String QUERY_CONTRACT_ERROR ="87720301";//查询签约信息失败
	public static final String TRAN_ID_NOT_EXIST ="87720302";//订单不存在
	
	public static final String ORDER_BUY_FAIL ="87720304";//交易失败
	
	public static final String NOT_BIND_FUND_SP = "87720305";  //没有绑定该基金公司
	public static final String TRANS_STATUS_ERROR = "87720306";  //订单状态错误（7状态）
	public static final String SP_ACCOUNT_STATUS_ERROR = "87720307";  //绑定的基金公司账户异常
	public static final String NOT_FIND_TRANS_ORDER ="87720308"; //没有查询到该订单
	public static final String MOBILE_ERROR ="87720309"; //手机号码跟财付通绑定的不一致
	public static final String NOT_AUTHED_USER ="87720310";  //非实名认证用户
	public FundMchapiWebRetException(String msg) {
		super(msg);
	}

	public FundMchapiWebRetException(String msg, Exception e) {
		super(msg, e);
	}

	public FundMchapiWebRetException(Exception e) {
		super(e);
	}
	
	public FundMchapiWebRetException(String retcode, String retmsg) {
		super(retcode, retmsg);
	}
}
