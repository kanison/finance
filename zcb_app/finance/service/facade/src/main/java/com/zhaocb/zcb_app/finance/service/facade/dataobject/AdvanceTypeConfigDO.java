package com.zhaocb.zcb_app.finance.service.facade.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class AdvanceTypeConfigDO implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4896852608983868877L;
	public int advanceId;
	public String spid;
	public BigDecimal spTotalQuoto;  //商户自有资金
	public BigDecimal usedSpQuoto;   //已用自有资金
	public BigDecimal creditQuoto;    //商户信用额度
	public BigDecimal usedCreditQuoto;  // 商户已用信用额度
	public BigDecimal creditQuotoOffset; // 可用额度阀值
	public Date createTime;
	public Date modifyTime;
	public int getAdvanceId() {
		return advanceId;
	}
	public void setAdvanceId(int advanceId) {
		this.advanceId = advanceId;
	}
	public String getSpid() {
		return spid;
	}
	public void setSpid(String spid) {
		this.spid = spid;
	}
	
	public BigDecimal getSpTotalQuoto() {
		return spTotalQuoto;
	}
	public void setSpTotalQuoto(BigDecimal spTotalQuoto) {
		this.spTotalQuoto = spTotalQuoto;
	}
	public BigDecimal getUsedSpQuoto() {
		return usedSpQuoto;
	}
	public void setUsedSpQuoto(BigDecimal usedSpQuoto) {
		this.usedSpQuoto = usedSpQuoto;
	}
	public BigDecimal getCreditQuoto() {
		return creditQuoto;
	}
	public void setCreditQuoto(BigDecimal creditQuoto) {
		this.creditQuoto = creditQuoto;
	}
	public BigDecimal getUsedCreditQuoto() {
		return usedCreditQuoto;
	}
	public void setUsedCreditQuoto(BigDecimal usedCreditQuoto) {
		this.usedCreditQuoto = usedCreditQuoto;
	}
	public BigDecimal getCreditQuotoOffset() {
		return creditQuotoOffset;
	}
	public void setCreditQuotoOffset(BigDecimal creditQuotoOffset) {
		this.creditQuotoOffset = creditQuotoOffset;
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
		
	
	
}
