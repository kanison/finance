package com.zhaocb.app.website.web.model;


public class DownBillInput {
	private String sign_type; // ǩ�����ͣ�ȡֵ��MD5��RSA��Ĭ�ϣ�MD5
	private String service_version; // �汾�ţ�Ĭ��Ϊ1.0
	private String input_charset; // �ַ�����,ȡֵ��GBK��UTF-8��Ĭ�ϣ�GBK
	private String sign; // ǩ��
	private int sign_key_index = 1; // ����Կ֧�ֵ���Կ��ţ�Ĭ��1
	private String partner;
	private String date;
	private String file_pre;

	
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String signType) {
		sign_type = signType;
	}
	public String getService_version() {
		return service_version;
	}
	public void setService_version(String serviceVersion) {
		service_version = serviceVersion;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public int getSign_key_index() {
		return sign_key_index;
	}
	public void setSign_key_index(int signKeyIndex) {
		sign_key_index = signKeyIndex;
	}
	public String getInput_charset() {
		return input_charset;
	}
	public void setInput_charset(String inputCharset) {
		input_charset = inputCharset;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getFile_pre() {
		return file_pre;
	}
	public void setFile_pre(String filePre) {
		file_pre = filePre;
	}
	
	
}
