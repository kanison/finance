package com.zcb_app.sms.service.facade.dataobject;

import java.io.Serializable;
import java.math.BigInteger;

public class VerifyCodeParams implements Serializable {
	
	private static final long serialVersionUID = 5746789598788396129L;

	private String mobile;//�ֻ���
	
	private BigInteger tmpl_id;//����ģ��ID
	
	private String verify_code;//��֤��
	
	private String relation_key;//�����Ĳ������·�ʱ���룬��֤ʱ���봫��
	
	private String client_ip;//�û�IP

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public BigInteger getTmpl_id() {
		return tmpl_id;
	}

	public void setTmpl_id(BigInteger tmpl_id) {
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

}
