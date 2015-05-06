package com.zhaocb.zcb_app.finance.fep.facade.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 财付通退票查询输出用户退票参数
 * 
 * @author zhl
 *
 */
public class PayRefundQueryUsersDO implements Serializable {

	private static final long serialVersionUID = 5443396857016093996L;

	private String draw_id;// 提现单号
	private String package_id;// 批次号
	private String serial;// 单笔序列号
	private Integer pay_amt;// 付款金额
	private String bank_type;// 银行编码
	private Date draw_time;// 代付发起时间
	private Date cancel_time;// 退票时间
	private String cancel_res;// 退票原因

	public String getDraw_id() {
		return draw_id;
	}

	public void setDraw_id(String draw_id) {
		this.draw_id = draw_id;
	}

	public String getPackage_id() {
		return package_id;
	}

	public void setPackage_id(String package_id) {
		this.package_id = package_id;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Integer getPay_amt() {
		return pay_amt;
	}

	public void setPay_amt(Integer pay_amt) {
		this.pay_amt = pay_amt;
	}

	public String getBank_type() {
		return bank_type;
	}

	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}

	public Date getDraw_time() {
		return draw_time;
	}

	public void setDraw_time(Date draw_time) {
		this.draw_time = draw_time;
	}

	public Date getCancel_time() {
		return cancel_time;
	}

	public void setCancel_time(Date cancel_time) {
		this.cancel_time = cancel_time;
	}

	public String getCancel_res() {
		return cancel_res;
	}

	public void setCancel_res(String cancel_res) {
		this.cancel_res = cancel_res;
	}

}
