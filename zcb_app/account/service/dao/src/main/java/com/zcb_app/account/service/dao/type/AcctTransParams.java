package com.zcb_app.account.service.dao.type;

import java.math.BigDecimal;
import java.util.Date;

import com.zcb_app.account.service.facade.dataobject.C2CTransParams;
import com.zcb_app.account.service.type.TransType;

public class AcctTransParams {

	private String voucher;//凭证单
	private String listid;//交易单
	private String req_no;
	private int cur_type;
	private BigDecimal trans_amt;
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
	private String rela_list;//关联单
	private String client_ip;//
	
	public String getVoucher() {
		return voucher;
	}
	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}
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
	public BigDecimal getTrans_amt() {
		return trans_amt;
	}
	public void setTrans_amt(BigDecimal trans_amt) {
		this.trans_amt = trans_amt;
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
	public int getFrom_acct_type() {
		return from_acct_type;
	}
	public void setFrom_acct_type(int from_acct_type) {
		this.from_acct_type = from_acct_type;
	}
	public int getTo_acct_type() {
		return to_acct_type;
	}
	public void setTo_acct_type(int to_acct_type) {
		this.to_acct_type = to_acct_type;
	}
	public String getRela_list() {
		return rela_list;
	}
	public void setRela_list(String rela_list) {
		this.rela_list = rela_list;
	}
	
	public String getClient_ip() {
		return client_ip;
	}
	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}
	public static AcctTransParams valueOf(C2CTransParams params)
	{
		AcctTransParams obj = new AcctTransParams();
		
		obj.setListid(params.getListid());
		obj.setReq_no(params.getReq_no());
		obj.setCur_type(params.getCur_type());
		obj.setTrans_amt(params.getPay_amt());
		obj.setFrozen_amt(params.getFrozen_amt());
		obj.setAction_type(params.getAction_type());
		obj.setTrans_type(TransType.TT_C2C_TRANS);
		obj.setFrom_uid(params.getFrom_uid());
		obj.setFrom_userid(params.getFrom_userid());
		obj.setFrom_acct_type(params.getFrom_acct_type());
		obj.setTo_uid(params.getTo_uid());
		obj.setTo_userid(params.getTo_userid());
		obj.setTo_acct_type(params.getTo_acct_type());
		obj.setTrade_acc_time(params.getTrade_acc_time());
		obj.setMemo(params.getMemo());
		obj.setRela_list(params.getFreeze_list());		
		
		return obj;
	}
}
