package com.zhaocb.common.aop.aspect.checklogin;

/**
 * 1.获得页面的cookie的qlskey 2.qlskey作为sessionKey获取session
 * 3.取得数据uin跟页面的qluin一致就判断登录有效,
 */
public class LoginSessionData {

	private int uinType; // 用户的登录类型, 1 – qq, 2 – email, 3 – 手机, …
	private String uin; // 登录账号
	private long uid; // 该用户的支付通内部ID
	private String loginTime; // 用户登录时的机器时间戳
	private String loginIp; // 用户登录的ip

	public static final int LOGIN_TYPE_CFT = 1;

	/**
	 * <p>
	 * <ul>
	 * <li>#define CFTLOGIN 0x1 //财付通完全登陆</li>
	 * <li>#define QQLOGIN 0x2 //非财付通帐号QQ登陆</li>
	 * <li>#define PRELOGIN 0x4 //初级登陆</li>
	 * </ul>
	 * </p>
	 * 如果牵涉资金的话选择CFTLOGIN|PRELOGIN，再弱一点你就>0都认为有过登陆<br>
	 * 目前可以通过设置，打开2级登陆体系，说白了就是两个密码,第一级权限受控<br>
	 * 如果设了2级的就是2级的，没有设的就一次<br>
	 * En,如果动用资金就要考虑验证这个态了<br>
	 * 如果只有0x2的话，不一定是财付通用户,仅仅表示qq登录。可能没开通财付通是吧<br>
	 */
	private int status; // 登录状态, 1 -- 有效登录状态，其它 – 无效

	public int getUinType() {
		return uinType;
	}

	public void setUinType(int uinType) {
		this.uinType = uinType;
	}

	public String getUin() {
		return uin;
	}

	public void setUin(String uin) {
		this.uin = uin;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}
}
