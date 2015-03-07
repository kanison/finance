package com.zcb_app.account.service.facade.dataobject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

public class UserAccountRollDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 508297065780636331L;
	public static final int TYPE_SAVE = 1;//入
	public static final int TYPE_DRAW = 2;//出
	public static final int TYPE_FREEZE =3;//冻结
	public static final int TYPE_UNFREEZE = 4; //解冻

	private long bkid;
	private long acctid;
	private long uid; // 用户uid
	private int account_type = 1;// 1 余额账户（默认） 其他未定义
	private int curtype = 156;// 人民币 -- 156(默认)
	private String listid;
	private String freeze_id;
	private String coding;
	private int subject;
	private int type;
	private BigDecimal balance;// 用户账户总金额（不包括授信金额，只是储蓄余额）金额保留两位小数
	private BigDecimal freeze_balance;// 冻结金额,金额保留两位小数
	private BigDecimal paynum;// 本次交易变更金额
	private BigDecimal freezenum;// 本次交易变更冻结金额
	private int state;// 用户状态
	private Date create_time;// 创建时间
	private Date modify_time;// 修改时间
	private String acc_time;
	private String memo;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getBkid() {
		return bkid;
	}

	public void setBkid(long bkid) {
		this.bkid = bkid;
	}

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

	public String getCoding() {
		return coding;
	}

	public void setCoding(String coding) {
		this.coding = coding;
	}

	public BigDecimal getPaynum() {
		return paynum;
	}

	public void setPaynum(BigDecimal paynum) {
		this.paynum = paynum;
	}

	public BigDecimal getFreezenum() {
		return freezenum;
	}

	public void setFreezenum(BigDecimal freezenum) {
		this.freezenum = freezenum;
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

	public String getListid() {
		return listid;
	}

	public void setListid(String listid) {
		this.listid = listid;
	}

	public String getFreeze_id() {
		return freeze_id;
	}

	public void setFreeze_id(String freezeId) {
		freeze_id = freezeId;
	}

	public int getSubject() {
		return subject;
	}

	public void setSubject(int subject) {
		this.subject = subject;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public String getAcc_time() {
		return acc_time;
	}

	public void setAcc_time(String accTime) {
		acc_time = accTime;
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
