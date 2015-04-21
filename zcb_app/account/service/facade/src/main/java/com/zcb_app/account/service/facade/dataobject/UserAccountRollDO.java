package com.zcb_app.account.service.facade.dataobject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

import com.app.utils.CommonUtil;

public class UserAccountRollDO implements Serializable {
	private static final long serialVersionUID = 508297065780636331L;

	private long pkid;
	private String listid;
	private long acctid;
	private long uid;
	private String userid;
	private int acct_type;
	private int cur_type;
	private long to_uid;
	private String to_userid;
	private BigDecimal pay_amt;
	private BigDecimal payfreeze_amt;
	private BigDecimal balance;
	private BigDecimal freeze_balance;
	private int action_type;
	private int trans_type;
	private int type;//记账类型
	private Date trade_acc_time;
	private Date create_time;// 创建时间
	private Date modify_time;// 修改时间
	private String memo;
	private String sign;
	private String rela_list;//交易关联的其他单号
	private String client_ip;
	
	private String db_idx;//分库名索引
	private String tb_idx;//分表名索引
	
	public long getPkid() {
		return pkid;
	}



	public void setPkid(long pkid) {
		this.pkid = pkid;
	}



	public String getListid() {
		return listid;
	}



	public void setListid(String listid) {
		this.listid = listid;
	}



	public long getAcctid() {
		return acctid;
	}



	public void setAcctid(long acctid) {
		this.acctid = acctid;
	}



	public long getUid() {
		return uid;
	}



	public void setUid(long uid) {
		this.uid = uid;
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



	public int getCur_type() {
		return cur_type;
	}



	public void setCur_type(int cur_type) {
		this.cur_type = cur_type;
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



	public BigDecimal getPay_amt() {
		return pay_amt;
	}



	public void setPay_amt(BigDecimal pay_amt) {
		this.pay_amt = pay_amt;
	}



	public BigDecimal getPayfreeze_amt() {
		return payfreeze_amt;
	}



	public void setPayfreeze_amt(BigDecimal payfreeze_amt) {
		this.payfreeze_amt = payfreeze_amt;
	}



	public BigDecimal getBalance() {
		return balance;
	}



	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}



	public BigDecimal getFreeze_balance() {
		return freeze_balance;
	}



	public void setFreeze_balance(BigDecimal freeze_balance) {
		this.freeze_balance = freeze_balance;
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



	public int getType() {
		return type;
	}



	public void setType(int type) {
		this.type = type;
	}



	public Date getTrade_acc_time() {
		return trade_acc_time;
	}



	public void setTrade_acc_time(Date trade_acc_time) {
		this.trade_acc_time = trade_acc_time;
	}



	public Date getCreate_time() {
		return create_time;
	}



	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}



	public Date getModify_time() {
		return modify_time;
	}



	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}



	public String getMemo() {
		return memo;
	}



	public void setMemo(String memo) {
		this.memo = memo;
	}



	public String getSign() {
		return sign;
	}



	public void setSign(String sign) {
		this.sign = sign;
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



	public String getDb_idx() {
		return db_idx;
	}



	public void setDb_idx(String db_idx) {
		this.db_idx = db_idx;
	}



	public String getTb_idx() {
		return tb_idx;
	}



	public void setTb_idx(String tb_idx) {
		this.tb_idx = tb_idx;
	}

	public void genDataTableIndex()
	{
		this.db_idx = CommonUtil.getDbIndexByUid(this.uid);
		this.tb_idx = CommonUtil.getTbIndexByUid(this.uid);		
	}

	public static void main(String[] args) {
		Field[] fields = UserAccountRollDO.class.getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		StringBuffer sbWithF = new StringBuffer();
		StringBuffer sbAndWithF = new StringBuffer();

		for (Field field : fields) {
			if ("serialVersionUID" != field.getName()) {
				sbWithF.append("F" + field.getName()).append(",");
				sb.append("#" + field.getName() + "#").append(",");
				sbAndWithF.append("F" + field.getName()).append("	").append(
						field.getName()).append(",").append("\n");
			}
		}

		sbWithF.deleteCharAt(sbWithF.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		System.out.println(sbWithF.toString());
		System.out.println(sb.toString());
		System.out.println(sbAndWithF.toString());

	}
}
