package com.zcb_app.sms.service.facade.dataobject;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Map;

public class SendCodeParams implements Serializable {
	
	private static final long serialVersionUID = 4734632036060034662L;

	private String mobile;//�ֻ���
	
	private BigInteger tmpl_id;//����ģ��ID
	
	private String client_ip;//�û�IP
	
	private String relation_key;//�����Ĳ������·�ʱ���룬��֤ʱ���봫��
	
	private String relation_info;//��������Ϣ������֤�ɹ���Ὣ����Ϣ����
	
	private int use_bak_port;//�Ƿ�ʹ�ñ�����������
	
	private Map<String, String> tmpl_value;//����ģ��Ĳ���ֵ����key-value����ʽ���ݸ���֤��ģ��

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

	public String getClient_ip() {
		return client_ip;
	}

	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}

	public String getRelation_key() {
		return relation_key;
	}

	public void setRelation_key(String relation_key) {
		this.relation_key = relation_key;
	}

	public String getRelation_info() {
		return relation_info;
	}

	public void setRelation_info(String relation_info) {
		this.relation_info = relation_info;
	}

	public int getUse_bak_port() {
		return use_bak_port;
	}

	public void setUse_bak_port(int use_bak_port) {
		this.use_bak_port = use_bak_port;
	}

	public Map<String, String> getTmpl_value() {
		return tmpl_value;
	}

	public void setTmpl_value(Map<String, String> tmpl_value) {
		this.tmpl_value = tmpl_value;
	}

}
