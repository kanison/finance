package com.zhaocb.zcb_app.finance.service.facade.dataobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TradeOrderDO implements Serializable {
	/**
	 * 
	 */
	public static int STATE_INIT=0;  // 创建订单
	public static int STATE_FINANCE_APPLY_SUC=1;  //垫资使用申请成功
	public static int STATE_SP_CONFIRM=2;	// 商户确认使用
	public static int STATE_FINANCE_RETURN=3;   // 商户未确认，资金退回到垫资账户
	public static int STATE_RECORD_FETCH_ORDER=4;  // 记录提现单
	public static int STATE_FETCHING=5;  // 提现中
	public static int STATE_FINANCE_USE_SUC=6;  // 垫资使用成功
	public static int STATE_FINANCE_USE_FAIL=7;  // 垫资使用失败
	public static int STATE_INVALID=20;   // 订单作废
	
	private static final long serialVersionUID = 6946461330962381594L;
	public String listId;
	public String bindId;
	public long uid;
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
	public String transferListId;
	public String freezeListId;
	public Date createTime;
	public Date modifyTime;
	public Date accTime;
	public String sign;
	public String memo;
	public int lastState;
	
	public int getLastState() {
		return lastState;
	}
	public void setLastState(int lastState) {
		this.lastState = lastState;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
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
	
	public String getTransferListId() {
		return transferListId;
	}
	public void setTransferListId(String transferListId) {
		this.transferListId = transferListId;
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
