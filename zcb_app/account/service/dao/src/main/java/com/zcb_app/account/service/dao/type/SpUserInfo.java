package com.zcb_app.account.service.dao.type;

/**
 * @author Tinffy Lee
 * 商户用户信息
 */
public class SpUserInfo {
	private long uid;
	private String spid;
	private int state;
	private long authflag;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getSpid() {
		return spid;
	}
	public void setSpid(String spid) {
		this.spid = spid;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public long getAuthflag() {
		return authflag;
	}
	public void setAuthflag(long authflag) {
		this.authflag = authflag;
	}	
}
