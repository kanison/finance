package com.zcb_app.account.service.facade.dataobject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

import com.zcb_app.account.service.type.AccountType;
import com.zcb_app.account.service.type.CurrencyType;

public class UserAccountDO implements Serializable {
	private static final long serialVersionUID = 8032719028686313731L;

	private long acctid;
	private long uid; // 用户uid
	private String userid;//用户名
	private int accttype = AccountType.UAT_NORMAL;// 1 普通余额账户（默认）
	private int curtype = CurrencyType.CNY;// 人民币 -- 156(默认)
	private BigDecimal balance;// 用户账户总金额（不包括授信金额，只是储蓄余额）金额保留两位小数
	private BigDecimal freeze_balance;// 冻结金额,金额保留两位小数
	private int state;// 用户状态
	private int lstate;// 物理逻辑状态
	private Date create_time;// 创建时间
	private Date modify_time;// 修改时间
	private String memo;
	private String sign;
	private Boolean querylock = false;//查询是否加锁
	
	public Boolean getQuerylock() {
		return querylock;
	}

	public void setQuerylock(Boolean querylock) {
		this.querylock = querylock;
	}

	public long getAcctid() {
		return acctid;
	}

	public void setAcctid(long acctid) {
		this.acctid = acctid;
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

	public int getAccttype() {
		return accttype;
	}

	public void setAccttype(int accttype) {
		this.accttype = accttype;
	}

	public int getCurtype() {
		return curtype;
	}

	public void setCurtype(int curtype) {
		this.curtype = curtype;
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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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
	
	public int getLstate() {
		return lstate;
	}

	public void setLstate(int lstate) {
		this.lstate = lstate;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
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
