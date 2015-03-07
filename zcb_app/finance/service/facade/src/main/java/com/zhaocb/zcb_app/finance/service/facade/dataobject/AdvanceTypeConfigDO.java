package com.zhaocb.zcb_app.finance.service.facade.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class AdvanceTypeConfigDO implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4896852608983868877L;
	public int advance_id;
	public String spid;
	public int lstate;
	public BigDecimal total_quoto;
	public BigDecimal available_quoto;
	public BigDecimal credit_quoto;
	public BigDecimal used_credit_quoto;
	public BigDecimal credit_quoto_offset; // 可用额度阀值
	public Date createTime;
	public Date modifyTime;
	
	public int getAdvance_id() {
		return advance_id;
	}
	public void setAdvance_id(int advance_id) {
		this.advance_id = advance_id;
	}
	public String getSpid() {
		return spid;
	}
	public void setSpid(String spid) {
		this.spid = spid;
	}
	public int getLstate() {
		return lstate;
	}
	public void setLstate(int lstate) {
		this.lstate = lstate;
	}
	public BigDecimal getTotal_quoto() {
		return total_quoto;
	}
	public void setTotal_quoto(BigDecimal total_quoto) {
		this.total_quoto = total_quoto;
	}
	public BigDecimal getAvailable_quoto() {
		return available_quoto;
	}
	public void setAvailable_quoto(BigDecimal available_quoto) {
		this.available_quoto = available_quoto;
	}
	public BigDecimal getCredit_quoto() {
		return credit_quoto;
	}
	public void setCredit_quoto(BigDecimal credit_quoto) {
		this.credit_quoto = credit_quoto;
	}
	public BigDecimal getUsed_credit_quoto() {
		return used_credit_quoto;
	}
	public void setUsed_credit_quoto(BigDecimal used_credit_quoto) {
		this.used_credit_quoto = used_credit_quoto;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public BigDecimal getCredit_quoto_offset() {
		return credit_quoto_offset;
	}
	public void setCredit_quoto_offset(BigDecimal credit_quoto_offset) {
		this.credit_quoto_offset = credit_quoto_offset;
	}
	
}
