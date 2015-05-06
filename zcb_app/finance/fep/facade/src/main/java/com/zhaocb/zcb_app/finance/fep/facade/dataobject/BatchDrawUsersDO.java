package com.zhaocb.zcb_app.finance.fep.facade.dataobject;

import java.io.Serializable;

/**
 * �Ƹ�ͨ�������������û��������
 * 
 * @author zhl
 *
 */
public class BatchDrawUsersDO implements Serializable {

	private static final long serialVersionUID = -9128529925716767683L;

	public String serial;// �������к�
	public String rec_bankacc;// �տ�����ʺ�
	public String bank_type;// ��������
	public String rec_name;// �տ��ʵ����
	public Integer pay_amt;// ������
	public String acc_type;// �˻�����(1Ϊ�����˻�,2Ϊ��˾�˻�)
	public String area;// ��������
	public String city;// ��������
	public String subbank_name;// ֧������
	public String desc;// ����˵��
	public String recv_mobile;// �������֪ͨ�ֻ���

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
