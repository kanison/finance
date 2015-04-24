package com.zcb_app.account.service.facade.dataobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.zcb_app.account.service.type.CurrencyType;

/**
 * 冻结接口输入参数对应实体Bean
 * @author Gu.Dongying
 */
public class FreezeBalanceParams implements Serializable {
	
	private static final long serialVersionUID = 3327943464645856946L;
	
	/*
	 * 冻结单
	 */
	private String listid;
	
	/*
	 * 币种  不传值时默认值为CNY（人民币）
	 */
	private int cur_type = CurrencyType.CNY;
	
	/*
	 * 冻结金额
	 */
	private BigDecimal freeze_amt;
	
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
	 * 操作码 一个固定的值，用于接口间的信认验证
	 */
	private String op_code;

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

	public String getOp_code() {
		return op_code;
	}

	public void setOp_code(String op_code) {
		this.op_code = op_code;
	}

}
