package com.zcb_app.account.service.facade.dataobject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

public class UserAccountDO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8032719028686313731L;

	private long acctid;
	private long uid; // 用户uid
	private int account_type = 1;// 1 余额账户（默认） 其他未定义
	private int curtype = 156;// 人民币 -- 156(默认)
	private BigDecimal balance;// 用户账户总金额（不包括授信金额，只是储蓄余额）金额保留两位小数
	private BigDecimal freeze_balance;// 冻结金额,金额保留两位小数
	// private BigDecimal credit_balance; //授信额度
	// private BigDecimal used_credit_balance;//已用授信额度（商户，或者用户都可以从平台借款）
	// private BigDecimal freeze_credit_balance;//冻结的授信额度
	private int state;// 用户状态
	private Date create_time;// 创建时间
	private Date modify_time;// 修改时间
	private String memo;

	public long getAcctid() {
		return acctid;
	}

	public void setAcctid(long acctid) {
		this.acctid = acctid;
	}

	public int getCurtype() {
		return curtype;
	}

	public void setCurtype(int curtype) {
		this.curtype = curtype;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getAccount_type() {
		return account_type;
	}

	public void setAccount_type(int accountType) {
		account_type = accountType;
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

	public void setFreeze_balance(BigDecimal freezeBalance) {
		freeze_balance = freezeBalance;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date createTime) {
		create_time = createTime;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modifyTime) {
		modify_time = modifyTime;
	}

	public static void main(String[] args) {
		Field[] fields = UserAccountDO.class.getDeclaredFields();
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
