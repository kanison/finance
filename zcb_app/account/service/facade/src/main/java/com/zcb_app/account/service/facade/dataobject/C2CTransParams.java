package com.zcb_app.account.service.facade.dataobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.zcb_app.account.service.type.CurrencyType;

public class C2CTransParams implements Serializable {

	private static final long serialVersionUID = -5750332344547811570L;

	private String listid;//交易单
	private String req_no;
	private int cur_type = CurrencyType.CNY;
	private BigDecimal pay_amt;
	private BigDecimal frozen_amt;
	private int action_type;//操作类型
	private int trans_type;//交易类型
	private long from_uid;
	private String from_userid;
	private int from_acct_type;//账户类型
	private long to_uid;
	private String to_userid;
	private int to_acct_type;//账户类型
	private Date trade_acc_time;
	private String memo;
	private String freeze_list;//冻结单
	private String auth_code;//授权码
	private String pay_pwd;//支付密码
	
	public String getListid() {
		return listid;
	}
	public void setListid(String listid) {
		this.listid = listid;
	}
	public String getReq_no() {
		return req_no;
	}
	public void setReq_no(String req_no) {
		this.req_no = req_no;
	}
	public int getCur_type() {
		return cur_type;
	}
	public void setCur_type(int cur_type) {
		this.cur_type = cur_type;
	}
	public BigDecimal getPay_amt() {
		return pay_amt;
	}
	public void setPay_amt(BigDecimal pay_amt) {
		this.pay_amt = pay_amt;
	}
	public BigDecimal getFrozen_amt() {
		return frozen_amt;
	}
	public void setFrozen_amt(BigDecimal frozen_amt) {
		this.frozen_amt = frozen_amt;
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
	public long getFrom_uid() {
		return from_uid;
	}
	public void setFrom_uid(long from_uid) {
		this.from_uid = from_uid;
	}
	public String getFrom_userid() {
		return from_userid;
	}
	public void setFrom_userid(String from_userid) {
		this.from_userid = from_userid;
	}
	public int getFrom_acct_type() {
		return from_acct_type;
	}
	public void setFrom_acct_type(int from_acct_type) {
		this.from_acct_type = from_acct_type;
	}
	public long getTo_uid() {
		return to_uid;
	}
	public void setTo_uid(long to_uid) {
		this.to_uid = to_uid;
	}
	public String getTo_userid() {
		return to_userid;
	}
	public void setTo_userid(String to_userid) {
		this.to_userid = to_userid;
	}
	public int getTo_acct_type() {
		return to_acct_type;
	}
	public void setTo_acct_type(int to_acct_type) {
		this.to_acct_type = to_acct_type;
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
	public String getFreeze_list() {
		return freeze_list;
	}
	public void setFreeze_list(String freeze_list) {
		this.freeze_list = freeze_list;
	}
	public String getAuth_code() {
		return auth_code;
	}
	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}
	public String getPay_pwd() {
		return pay_pwd;
	}
	public void setPay_pwd(String pay_pwd) {
		this.pay_pwd = pay_pwd;
	}	
}
