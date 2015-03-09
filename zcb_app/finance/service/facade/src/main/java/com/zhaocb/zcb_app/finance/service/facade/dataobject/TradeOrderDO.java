package com.zhaocb.zcb_app.finance.service.facade.dataobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TradeOrderDO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6946461330962381594L;
	public String listId;
	public String bindId;
	public String spid;
	public String bizCode;
	public String outTradeNo;
	public BigDecimal totalFee;
	public int useType;
	public int useDays;
	public int moneyType;
	public BigDecimal chargeFee;
	public int chargeType;
	public String rateId;
	public int bankType;
	public String cardNo;
	public String trueName;
	public int state;
	public String fetchLlistId;
	public String advanceListId;
	public String freezeListId;
	public Date createTime;
	public Date modifyTime;
	public Date accTime;
	public String sign;
	public String memo;
	
	public String getListId() {
		return listId;
	}
	public void setListId(String listId) {
		this.listId = listId;
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
	public int getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(int moneyType) {
		this.moneyType = moneyType;
	}
	public BigDecimal getChargeFee() {
		return chargeFee;
	}
	public void setChargeFee(BigDecimal chargeFee) {
		this.chargeFee = chargeFee;
	}
	public int getChargeType() {
		return chargeType;
	}
	public void setChargeType(int chargeType) {
		this.chargeType = chargeType;
	}
	public String getRateId() {
		return rateId;
	}
	public void setRateId(String rateId) {
		this.rateId = rateId;
	}
	public int getBankType() {
		return bankType;
	}
	public void setBankType(int bankType) {
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
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getFetchLlistId() {
		return fetchLlistId;
	}
	public void setFetchLlistId(String fetchLlistId) {
		this.fetchLlistId = fetchLlistId;
	}
	public String getAdvanceListId() {
		return advanceListId;
	}
	public void setAdvanceListId(String advanceListId) {
		this.advanceListId = advanceListId;
	}
	public String getFreezeListId() {
		return freezeListId;
	}
	public void setFreezeListId(String freezeListId) {
		this.freezeListId = freezeListId;
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
	public Date getAccTime() {
		return accTime;
	}
	public void setAccTime(Date accTime) {
		this.accTime = accTime;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}
