package com.zhaocb.zcb_app.finance.service.facade.dataobject;

import java.io.Serializable;
import java.util.Date;

public class UserInfoDO implements Serializable {

	private static final long serialVersionUID = -6125404297767477834L;

	public long userId;
	public String userName;
	public String trueName;
	public String userPwd;
	public String userPayPassword;
	public int sex;// 性别1男 2 女
	public String mobile;
	public String email;
	public String phone;
	public int creType;// 证件类型 1身份证
	public String creId;
	public int state;// 用户状态 1正常2冻结3注销
	public int logicState;// 物理逻辑状态 1有效2无效
	public String regIp;
	public String userSeed;
	public String sign;
	public Date createTime;
	public Date modifyTime;
	public String loginIp;
	public Date loginTime;
	public Date pwdModTime;
	public String pwdModIp;
	public Date payPwdModTime;
	public Date payPwdModIp;
	public String question1;
	public String answer1;
	public String memo;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserPayPassword() {
		return userPayPassword;
	}

	public void setUserPayPassword(String userPayPassword) {
		this.userPayPassword = userPayPassword;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getCreType() {
		return creType;
	}

	public void setCreType(int creType) {
		this.creType = creType;
	}

	public String getCreId() {
		return creId;
	}

	public void setCreId(String creId) {
		this.creId = creId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getLogicState() {
		return logicState;
	}

	public void setLogicState(int logicState) {
		this.logicState = logicState;
	}

	public String getRegIp() {
		return regIp;
	}

	public void setRegIp(String regIp) {
		this.regIp = regIp;
	}

	public String getUserSeed() {
		return userSeed;
	}

	public void setUserSeed(String userSeed) {
		this.userSeed = userSeed;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getPwdModTime() {
		return pwdModTime;
	}

	public void setPwdModTime(Date pwdModTime) {
		this.pwdModTime = pwdModTime;
	}

	public String getPwdModIp() {
		return pwdModIp;
	}

	public void setPwdModIp(String pwdModIp) {
		this.pwdModIp = pwdModIp;
	}

	public Date getPayPwdModTime() {
		return payPwdModTime;
	}

	public void setPayPwdModTime(Date payPwdModTime) {
		this.payPwdModTime = payPwdModTime;
	}

	public Date getPayPwdModIp() {
		return payPwdModIp;
	}

	public void setPayPwdModIp(Date payPwdModIp) {
		this.payPwdModIp = payPwdModIp;
	}

	public String getQuestion1() {
		return question1;
	}

	public void setQuestion1(String question1) {
		this.question1 = question1;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}
