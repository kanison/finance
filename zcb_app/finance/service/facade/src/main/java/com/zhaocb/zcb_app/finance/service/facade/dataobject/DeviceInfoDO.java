package com.zhaocb.zcb_app.finance.service.facade.dataobject;

import java.io.Serializable;
import java.util.Date;

public class DeviceInfoDO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 947883012683853268L;

	private long itemId;
	private long deviceId;
	private String imei;
	private String mac;
	private String mac2;
	private String extr;
	private int appVer;
	private String channelId;
	private int platType;
	private String clientIp;
	private int state;
	private Date createTime;
	private Date modifyTime;
	public long getItemId() {
		return itemId;
	}
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	public long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getMac2() {
		return mac2;
	}
	public void setMac2(String mac2) {
		this.mac2 = mac2;
	}
	public String getExtr() {
		return extr;
	}
	public void setExtr(String extr) {
		this.extr = extr;
	}
	public int getAppVer() {
		return appVer;
	}
	public void setAppVer(int appVer) {
		this.appVer = appVer;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public int getPlatType() {
		return platType;
	}
	public void setPlatType(int platType) {
		this.platType = platType;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
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
}
