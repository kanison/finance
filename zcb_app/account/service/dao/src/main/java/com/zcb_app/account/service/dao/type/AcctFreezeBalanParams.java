package com.zcb_app.account.service.dao.type;

import java.math.BigDecimal;
import java.util.Date;

import com.zcb_app.account.service.facade.dataobject.FreezeBalanceParams;
import com.zcb_app.account.service.type.TransType;

public class AcctFreezeBalanParams {
	/*
	 * 冻结单
	 */
	private String listid;
	
	/*
	 * 币种  不传值时默认值为CNY（人民币）
	 */
	private int cur_type;
	
	/*
	 * 冻结金额
	 */
	private BigDecimal freeze_amt;
	
	private long uid;
	
	/*
	 * 用户名
	 */
	private String userid;
	
	/*
	 * 用户账户类型 账户类型不能为银行账户
	 */
	private int acct_type;
	
	/*
	 * 交易时间
	 */
	private Date trade_acc_time;
	
	/*
	 * 备注
	 */
	private String memo;
	
	/*
	 * 冻结原因
	 */
	private int freeze_reason;
	
	/*
	 * 冻结操作类型
	 */
	private int action_type = TransType.ACT_FREEZE;
	
	/*
	 * 交易类型
	 */
	private int trans_type = TransType.TT_FREEZE;
	
	/*
	 * 冻结用户客户端IP
	 */
	private String client_ip;
	

	public String getListid() {
		return listid;
	}

	public void setListid(String listid) {
		this.listid = listid;
	}

	public int getCur_type() {
		return cur_type;
	}

	public void setCur_type(int cur_type) {
		this.cur_type = cur_type;
	}

	public BigDecimal getFreeze_amt() {
		return freeze_amt;
	}

	public void setFreeze_amt(BigDecimal freeze_amt) {
		this.freeze_amt = freeze_amt;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getAcct_type() {
		return acct_type;
	}

	public void setAcct_type(int acct_type) {
		this.acct_type = acct_type;
	}

	public Date getTrade_acc_time() {
		return trade_acc_time;
	}

	public void setTrade_acc_time(Date trade_acc_time) {
		this.trade_acc_time = trade_acc_time;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getFreeze_reason() {
		return freeze_reason;
	}

	public void setFreeze_reason(int freeze_reason) {
		this.freeze_reason = freeze_reason;
	}

	public int getAction_type() {
		return action_type;
	}

	public void setAction_type(int action_type) {
		this.action_type = action_type;
	}

	public String getClient_ip() {
		return client_ip;
	}

	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getTrans_type() {
		return trans_type;
	}

	public void setTrans_type(int trans_type) {
		this.trans_type = trans_type;
	}

	public static AcctFreezeBalanParams valueOf(FreezeBalanceParams params){
		AcctFreezeBalanParams obj = new AcctFreezeBalanParams();
		obj.setAcct_type(params.getAcct_type());
		obj.setCur_type(params.getCur_type());
		obj.setFreeze_amt(params.getFreeze_amt());
		obj.setListid(params.getListid());
		obj.setMemo(params.getMemo());
		obj.setTrade_acc_time(params.getTrade_acc_time());
		obj.setUserid(params.getUserid());
		return obj;
	}
}
