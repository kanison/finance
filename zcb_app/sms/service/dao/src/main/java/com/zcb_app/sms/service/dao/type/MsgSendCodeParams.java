package com.zcb_app.sms.service.dao.type;

import java.util.Date;

import com.zcb_app.sms.service.facade.dataobject.SendCodeParams;

public class MsgSendCodeParams {

	private String mobile;//�ֻ���
	
	private Long tmpl_id;//����ģ��ID
	
	private String verify_code;//��֤��
	
	private String relation_key;//�����Ĳ������·�ʱ���룬��֤ʱ���봫��
	
	private String relation_info;
	
	private String client_ip;//�û�IP
	
	private int chk_suc_times;//У��ɹ��Ĵ���
	
	private int chk_err_times;//У��ʧ�ܵĴ���
	
	private Date send_time;//�·�ʱ��
	
	private Date check_time;//���һ����֤ʱ��
	
	private int expired_time;//��֤�����ʱ��
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getTmpl_id() {
		return tmpl_id;
	}

	public void setTmpl_id(Long tmpl_id) {
		this.tmpl_id = tmpl_id;
	}

	public String getVerify_code() {
		return verify_code;
	}

	public void setVerify_code(String verify_code) {
		this.verify_code = verify_code;
	}

	public String getRelation_key() {
		return relation_key;
	}

	public void setRelation_key(String relation_key) {
		this.relation_key = relation_key;
	}

	public String getClient_ip() {
		return client_ip;
	}

	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}

	public int getChk_suc_times() {
		return chk_suc_times;
	}

	public void setChk_suc_times(int chk_suc_times) {
		this.chk_suc_times = chk_suc_times;
	}

	public int getChk_err_times() {
		return chk_err_times;
	}

	public void setChk_err_times(int chk_err_times) {
		this.chk_err_times = chk_err_times;
	}

	public Date getSend_time() {
		return send_time;
	}

	public void setSend_time(Date send_time) {
		this.send_time = send_time;
	}

	public Date getCheck_time() {
		return check_time;
	}

	public void setCheck_time(Date check_time) {
		this.check_time = check_time;
	}

	public int getExpired_time() {
		return expired_time;
	}

	public void setExpired_time(int expired_time) {
		this.expired_time = expired_time;
	}

	public String getRelation_info() {
		return relation_info;
	}

	public void setRelation_info(String relation_info) {
		this.relation_info = relation_info;
	}
	
	public static MsgSendCodeParams valueOf(SendCodeParams params){
		MsgSendCodeParams send = new MsgSendCodeParams();
		send.setCheck_time(new Date());
		send.setClient_ip(params.getClient_ip());
		send.setMobile(params.getMobile());
		send.setRelation_key(params.getRelation_key());
		send.setRelation_info(params.getRelation_info());
		send.setTmpl_id(params.getTmpl_id());
		send.setSend_time(new Date());
		return send;
	}
	
}
