package com.zcb_app.sms.service.facade.dataobject;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Map;

public class SendMessageParams implements Serializable {
	
	private static final long serialVersionUID = 5184718068224269517L;

	private String mobile;//�ֻ���
	
	private BigInteger tmpl_id;//����ģ��ID
	
	private String client_ip;//�û�IP
	
	private int use_bak_port;//�Ƿ�ʹ�ñ�����������
	
	private Map<String, String> tmpl_value;//����ģ��Ĳ���ֵ����key=value����ʽ���� 
	
	private String op_code;//�ڲ�����

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

	public String getOp_code() {
		return op_code;
	}

	public void setOp_code(String op_code) {
		this.op_code = op_code;
	}
}
