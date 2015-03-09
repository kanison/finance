package com.zhaocb.zcb_app.finance.service.facade.dataobject;

import java.io.Serializable;

public class SpBizConfigDO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2927833138870413703L;
	public String spid;
	public String bizCode; // 业务号
	public int lstate;  // 该业务是否可用 1 可用; 2 不可用
	public int advanceId;   // 垫资类型id
	public String createTime;
	public String modifyTime;
	public String type;
	public String rateId;
	public String chargeRate;
	public String chargeFee;
	public String chargeType;
	public String bankType;  // 商户回补银行的银行类型
	public String cardNo;    // 回补银行卡号
	public String trueName;  // 回补持卡人姓名
	public String bankName;  // 银行名称
	public String memo;
	public String getSpid() {
		return spid;
	}
	public void setSpid(String spid) {
		this.spid = spid;
	}
	public String getBizCode() {
		return bizCode;
	}
	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}
	public int getLstate() {
		return lstate;
	}
	public void setLstate(int lstate) {
		this.lstate = lstate;
	}
	public int getAdvanceId() {
		return advanceId;
	}
	public void setAdvanceId(int advanceId) {
		this.advanceId = advanceId;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRateId() {
		return rateId;
	}
	public void setRateId(String rateId) {
		this.rateId = rateId;
	}
	public String getChargeRate() {
		return chargeRate;
	}
	public void setChargeRate(String chargeRate) {
		this.chargeRate = chargeRate;
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
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
	
}
