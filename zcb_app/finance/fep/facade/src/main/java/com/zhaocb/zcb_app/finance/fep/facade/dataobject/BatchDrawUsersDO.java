package com.zhaocb.zcb_app.finance.fep.facade.dataobject;

import java.io.Serializable;

/**
 * 财付通付款申请输入用户付款参数
 * 
 * @author zhl
 *
 */
public class BatchDrawUsersDO implements Serializable {

	private static final long serialVersionUID = -9128529925716767683L;

	public String serial;// 单笔序列号
	public String rec_bankacc;// 收款方银行帐号
	public String bank_type;// 银行类型
	public String rec_name;// 收款方真实姓名
	public Integer pay_amt;// 付款金额
	public String acc_type;// 账户类型(1为个人账户,2为公司账户)
	public String area;// 开户地区
	public String city;// 开户城市
	public String subbank_name;// 支行名称
	public String desc;// 付款说明
	public String recv_mobile;// 付款接收通知手机号

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getRec_bankacc() {
		return rec_bankacc;
	}

	public void setRec_bankacc(String rec_bankacc) {
		this.rec_bankacc = rec_bankacc;
	}

	public String getBank_type() {
		return bank_type;
	}

	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}

	public String getRec_name() {
		return rec_name;
	}

	public void setRec_name(String rec_name) {
		this.rec_name = rec_name;
	}

	public Integer getPay_amt() {
		return pay_amt;
	}

	public void setPay_amt(Integer pay_amt) {
		this.pay_amt = pay_amt;
	}

	public String getAcc_type() {
		return acc_type;
	}

	public void setAcc_type(String acc_type) {
		this.acc_type = acc_type;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSubbank_name() {
		return subbank_name;
	}

	public void setSubbank_name(String subbank_name) {
		this.subbank_name = subbank_name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getRecv_mobile() {
		return recv_mobile;
	}

	public void setRecv_mobile(String recv_mobile) {
		this.recv_mobile = recv_mobile;
	}

}
