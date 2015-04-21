package com.zcb_app.account.service.facade.dataobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.app.utils.CommonUtil;

public class FreezeListDO implements Serializable {

	private static final long serialVersionUID = 5626455176276459254L;

	private long pkid;
	private String listid;//交易单
	private long uid;
	private String userid;
	private int cur_type;
	private BigDecimal freeze_amt;
	private BigDecimal unfreeze_amt;
	private int action_type;//操作类型
	private int reason;//冻结原因
	private String rela_list;
	private String memo;
	private String ip;
	private int state;
	private Date unfreeze_time;
	private String unfreeze_memo;
	private String unfreeze_ip;
	private Date create_time;// 创建时间
	private Date modify_time;// 修改时间
	private String sign;
	
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
	public BigDecimal getUnfreeze_amt() {
		return unfreeze_amt;
	}
	public void setUnfreeze_amt(BigDecimal unfreeze_amt) {
		this.unfreeze_amt = unfreeze_amt;
	}
	public int getAction_type() {
		return action_type;
	}
	public void setAction_type(int action_type) {
		this.action_type = action_type;
	}
	public int getReason() {
		return reason;
	}
	public void setReason(int reason) {
		this.reason = reason;
	}
	public String getRela_list() {
		return rela_list;
	}
	public void setRela_list(String rela_list) {
		this.rela_list = rela_list;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Date getUnfreeze_time() {
		return unfreeze_time;
	}
	public void setUnfreeze_time(Date unfreeze_time) {
		this.unfreeze_time = unfreeze_time;
	}
	public String getUnfreeze_memo() {
		return unfreeze_memo;
	}
	public void setUnfreeze_memo(String unfreeze_memo) {
		this.unfreeze_memo = unfreeze_memo;
	}
	public String getUnfreeze_ip() {
		return unfreeze_ip;
	}
	public void setUnfreeze_ip(String unfreeze_ip) {
		this.unfreeze_ip = unfreeze_ip;
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
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
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
		this.db_idx = CommonUtil.getDbIndexByLisid(this.listid);
		this.tb_idx = CommonUtil.getTbIndexByLisid(this.listid);		
	}
}
