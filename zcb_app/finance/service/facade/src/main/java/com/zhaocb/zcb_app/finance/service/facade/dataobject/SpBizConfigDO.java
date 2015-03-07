package com.zhaocb.zcb_app.finance.service.facade.dataobject;

import java.io.Serializable;

public class SpBizConfigDO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2927833138870413703L;
	public String spid;
	public String biz_code; // 业务号
	public int lstate;  // 该业务是否可用 1 可用; 2 不可用
	public String advance_id;   // 垫资类型id
	public String createTime;
	public String modifyTime;
	public String type;
	public String rate_id;
	public String charge_rate;
	public String charge_fee;
	public String charge_type;
	public String bank_type;
	public String card_no;
	public String true_name;
	public String memo;
	public String getSpid() {
		return spid;
	}
	public void setSpid(String spid) {
		this.spid = spid;
	}
	public String getBiz_code() {
		return biz_code;
	}
	public void setBiz_code(String biz_code) {
		this.biz_code = biz_code;
	}
	public int getLstate() {
		return lstate;
	}
	public void setLstate(int lstate) {
		this.lstate = lstate;
	}
	public String getAdvance_id() {
		return advance_id;
	}
	public void setAdvance_id(String advance_id) {
		this.advance_id = advance_id;
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
	public String getRate_id() {
		return rate_id;
	}
	public void setRate_id(String rate_id) {
		this.rate_id = rate_id;
	}
	public String getCharge_rate() {
		return charge_rate;
	}
	public void setCharge_rate(String charge_rate) {
		this.charge_rate = charge_rate;
	}
	public String getCharge_fee() {
		return charge_fee;
	}
	public void setCharge_fee(String charge_fee) {
		this.charge_fee = charge_fee;
	}
	public String getCharge_type() {
		return charge_type;
	}
	public void setCharge_type(String charge_type) {
		this.charge_type = charge_type;
	}
	public String getBank_type() {
		return bank_type;
	}
	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	public String getTrue_name() {
		return true_name;
	}
	public void setTrue_name(String true_name) {
		this.true_name = true_name;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}
