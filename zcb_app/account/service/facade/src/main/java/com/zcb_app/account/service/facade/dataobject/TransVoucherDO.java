package com.zcb_app.account.service.facade.dataobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.app.utils.CommonUtil;
import com.tenpay.sm.common.lang.StringUtil;

public class TransVoucherDO implements Serializable {
	private static final long serialVersionUID = 1671854126487406757L;
	
	private long pkid;
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
	private long to_uid;
	private String to_userid;	
	private Date trade_acc_time;
	private Date create_time;// 创建时间
	private Date modify_time;// 修改时间
	private String memo;
	private String rela_list;
	
	private String db_idx;//分库名索引
	private String tb_idx;//分表名索引
	
	public long getPkid() {
		return pkid;
	}
	public void setPkid(long pkid) {
		this.pkid = pkid;
	}
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
	public String getRela_list() {
		return rela_list;
	}
	public void setRela_list(String rela_list) {
		this.rela_list = rela_list;
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
	
	/**
	 * 交易凭证单，Faction_type作为前缀(4位长度)+Flistid
	 * 
	 * @author Gu.Dongying 
	 * @Date 2015年5月8日 上午11:20:35
	 */
	public void genVoucher(){
		if(StringUtil.isNotBlank(this.listid) && StringUtil.isNotEmpty(this.listid)){
			this.setVoucher(CommonUtil.lpad(4, this.action_type) + this.listid);
		}
	}
	
	public void genDataTableIndex()
	{
		this.db_idx = CommonUtil.getDbIndexByLisid(this.listid);
		this.tb_idx = CommonUtil.getTbIndexByLisid(this.listid);		
	}
}
