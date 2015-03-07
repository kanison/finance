package com.zhaocb.app.website.web.model;

import java.math.BigDecimal;

public class UseFinanceOutput {
	private String spid;
	private String spUserId; // �û����̻�ϵͳ��id
	private String bizCode; // �̻�ҵ��id
	private String outTradeNo; // �̻�����
	private BigDecimal totalFee; 
	private String listid; // ����ϵͳ����
	private int useType; // ����ʹ������ 1 t+1�黹  2 ������ʹ��
	private int useDays; // �ʽ�ʹ������ 
	private String chargeFee; // ������
	private String chargeType; // ����������
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
