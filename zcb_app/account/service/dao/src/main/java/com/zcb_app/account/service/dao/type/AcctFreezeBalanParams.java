package com.zcb_app.account.service.dao.type;

import java.math.BigDecimal;
import java.util.Date;

import com.zcb_app.account.service.facade.dataobject.FreezeBalanceParams;
import com.zcb_app.account.service.type.TransType;

public class AcctFreezeBalanParams {
	/*
	 * ���ᵥ
	 */
	private String listid;
	
	/*
	 * ����  ����ֵʱĬ��ֵΪCNY������ң�
	 */
	private int cur_type;
	
	/*
	 * ������
	 */
	private BigDecimal freeze_amt;
	
	private long uid;
	
	/*
	 * �û���
	 */
	private String userid;
	
	/*
	 * �û��˻����� �˻����Ͳ���Ϊ�����˻�
	 */
	private int acct_type;
	
	/*
	 * ����ʱ��
	 */
	private Date trade_acc_time;
	
	/*
	 * ��ע
	 */
	private String memo;
	
	/*
	 * ����ԭ��
	 */
	private int freeze_reason;
	
	/*
	 * �����������
	 */
	private int action_type = TransType.ACT_FREEZE;
	
	/*
	 * ��������
	 */
	private int trans_type = TransType.TT_FREEZE;
	
	/*
	 * �����û��ͻ���IP
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
