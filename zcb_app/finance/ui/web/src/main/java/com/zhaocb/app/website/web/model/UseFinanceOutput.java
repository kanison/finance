package com.zhaocb.app.website.web.model;

import java.math.BigDecimal;

public class UseFinanceOutput {
	private String spid;
	private String spUserId; // 用户在商户系统的id
	private String bizCode; // 商户业务id
	private String outTradeNo; // 商户单号
	private BigDecimal totalFee; 
	private String listid; // 垫资系统单号
	private int useType; // 垫资使用类型 1 t+1归还  2 按天数使用
	private int useDays; // 资金使用天数 
	private String chargeFee; // 手续费
	private String chargeType; // 手续费类型
	private String accTime;
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
	public String getBizCode() {
		return bizCode;
	}
	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	
	public BigDecimal getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}
	public String getListid() {
		return listid;
	}
	public void setListid(String listid) {
		this.listid = listid;
	}
	public int getUseType() {
		return useType;
	}
	public void setUseType(int useType) {
		this.useType = useType;
	}
	public int getUseDays() {
		return useDays;
	}
	public void setUseDays(int useDays) {
		this.useDays = useDays;
	}
	public String getChargeFee() {
		return chargeFee;
	}
	public void setChargeFee(String chargeFee) {
		this.chargeFee = chargeFee;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	public String getAccTime() {
		return accTime;
	}
	public void setAccTime(String accTime) {
		this.accTime = accTime;
	}
	
}
