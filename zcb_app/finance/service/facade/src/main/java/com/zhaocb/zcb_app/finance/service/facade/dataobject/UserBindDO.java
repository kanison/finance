package com.zhaocb.zcb_app.finance.service.facade.dataobject;

import java.io.Serializable;
import java.util.Date;

public class UserBindDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8836329996316392796L;
	public long pkid;
	public String bindId;
	public String spid;
	public String spUserId;
	public int lstate=1;  // 1 有效   2 无效  
	public int creType;
	public String creId;
	public String trueName;
	public String phone;
	public String mobile;
	public Date accTime;
	public String createTime;
	public String modifyTime;
	public String memo;

	public long getPkid() {
		return pkid;
	}
	public void setPkid(long pkid) {
		this.pkid = pkid;
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
	
	public int getLstate() {
		return lstate;
	}
	public void setLstate(int lstate) {
		this.lstate = lstate;
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
	
	public Date getAccTime() {
		return accTime;
	}
	public void setAccTime(Date accTime) {
		this.accTime = accTime;
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
