package com.zhaocb.zcb_app.finance.service.facade.dataobject;

import java.io.Serializable;

public class UserBindDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8836329996316392796L;
	public String uid;
	public String bindId;
	public String spid;
	public String spUserId;
	public String lstate;
	public String creType;
	public String creId;
	public String trueName;
	public String phone;
	public String mobile;
	public String acc_time;
	public String createTime;
	public String modifyTime;
	public String memo;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getBindId() {
		return bindId;
	}
	public void setBindId(String bindId) {
		this.bindId = bindId;
	}
	public String getSpid() {
		return spid;
	}
	public void setSpid(String spid) {
		this.spid = spid;
	}
	public String getSpUserId() {
		return spUserId;
	}
	public void setSpUserId(String spUserId) {
		this.spUserId = spUserId;
	}
	public String getLstate() {
		return lstate;
	}
	public void setLstate(String lstate) {
		this.lstate = lstate;
	}
	public String getCreType() {
		return creType;
	}
	public void setCreType(String creType) {
		this.creType = creType;
	}
	public String getCreId() {
		return creId;
	}
	public void setCreId(String creId) {
		this.creId = creId;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAcc_time() {
		return acc_time;
	}
	public void setAcc_time(String acc_time) {
		this.acc_time = acc_time;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}
