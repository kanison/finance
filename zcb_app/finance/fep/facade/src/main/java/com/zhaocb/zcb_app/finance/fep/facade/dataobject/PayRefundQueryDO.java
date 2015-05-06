package com.zhaocb.zcb_app.finance.fep.facade.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 财付通退票查询输入参数
 * 
 * @author zhl
 *
 */
public class PayRefundQueryDO extends PayRefundQueryProtocolDO implements
		Serializable {

	private static final long serialVersionUID = -2875102871135958584L;

	public String partner;// 商户号
	public Date start_time;// 开始时间
	public Date end_time;// 结束时间
	public String bank_type;// 银行编码
	public String rec_bankacc;// 收款方银行帐号
	public String rec_name;// 收款方真实姓名

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public String getBank_type() {
		return bank_type;
	}

	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}

	public String getRec_bankacc() {
		return rec_bankacc;
	}

	public void setRec_bankacc(String rec_bankacc) {
		this.rec_bankacc = rec_bankacc;
	}

	public String getRec_name() {
		return rec_name;
	}

	public void setRec_name(String rec_name) {
		this.rec_name = rec_name;
	}

}
