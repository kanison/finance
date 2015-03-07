package com.zhaocb.app.website.web.model;

import java.math.BigDecimal;

public class UseFinanceInput {
	private String spid;
	private String spUserId; // �û����̻�ϵͳ��id
	private String bizCode; // �̻�ҵ��id
	private String outTradeNo; // �̻�����
	private BigDecimal totalFee; 
	private String listid; // ����ϵͳ����
	private int useType; // ����ʹ������ 1 t+1�黹  2 ������ʹ��
	private int useDays; // �ʽ�ʹ������ 
	private String bankType;
	private String cardNo;
	private String trueName;
	private int creType;
	private String creId;
	private String mobile;
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
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAccTime() {
		return accTime;
	}
	public void setAccTime(String accTime) {
		this.accTime = accTime;
	}
	
}
