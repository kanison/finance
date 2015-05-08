package com.zcb_app.account.service.dao.type;

import java.math.BigDecimal;
import java.util.Date;

import com.zcb_app.account.service.facade.dataobject.UnFreezeBalanceParams;
import com.zcb_app.account.service.type.TransType;

public class AcctUnFreezeBalanceParams {

	private long uid;
	
	private String listid;//解冻单号 当进行部分解冻时，解冻单号由于业务自己生成 当进行全部解冻时，解冻单号可以使用原冻结单号。
	
	private int cur_type;//币种 不传值时默认值为CNY（人民币）
	
	private BigDecimal unfreeze_amt;//解冻金额
	
	private String userid;//用户名
	
	private int acct_type;//用户账户类型 账户类型不能为银行账户
	
	private String freeze_list;//原冻结单号
	
	private Date trade_acc_time;//交易时间
	
	private String memo;//备注
	
	/*
	 * 冻结操作类型
	 */
	private int action_type = TransType.ACT_UNFREEZE;
	
	/*
	 * 交易类型
	 */
	private int trans_type = TransType.TT_UNFREEZ;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

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

	public BigDecimal getUnfreeze_amt() {
		return unfreeze_amt;
	}

	public void setUnfreeze_amt(BigDecimal unfreeze_amt) {
		this.unfreeze_amt = unfreeze_amt;
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

	public String getFreeze_list() {
		return freeze_list;
	}

	public void setFreeze_list(String freeze_list) {
		this.freeze_list = freeze_list;
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
	
	public int getAction_type() {
		return action_type;
	}

	public void setAction_type(int action_type) {
		this.action_type = action_type;
	}

	public int getTrans_type() {
		return trans_type;
	}

	public void setTrans_type(int trans_type) {
		this.trans_type = trans_type;
	}

	public static AcctUnFreezeBalanceParams valueOf(UnFreezeBalanceParams unFreeze){
		AcctUnFreezeBalanceParams acctUnFreeze = new AcctUnFreezeBalanceParams();
		acctUnFreeze.setAcct_type(unFreeze.getAcct_type());
		acctUnFreeze.setCur_type(unFreeze.getCur_type());
		acctUnFreeze.setFreeze_list(unFreeze.getFreeze_list());
		acctUnFreeze.setListid(unFreeze.getListid());
		acctUnFreeze.setMemo(unFreeze.getMemo());
		acctUnFreeze.setTrade_acc_time(unFreeze.getTrade_acc_time());
		acctUnFreeze.setUnfreeze_amt(unFreeze.getUnfreeze_amt());
		acctUnFreeze.setUserid(unFreeze.getUserid());
		return acctUnFreeze;
	}
}
