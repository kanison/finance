package com.zhaocb.zcb_app.finance.fep.facade.dataobject;

import java.io.Serializable;

/**
 * �Ƹ�ͨ��Ʊ��ѯЭ�����
 * 
 * @author zhl
 *
 */
public class PayRefundQueryProtocolDO implements Serializable {

	private static final long serialVersionUID = 5443396857016093996L;

	public String sign_type;// ǩ������,ȡֵ��MD5��RSA��Ĭ�ϣ�MD5
	public String service_version;// �汾��,Ĭ��Ϊ1.0
	public String input_charset;// �ַ�����,ȡֵ��GBK��UTF-8,Ĭ��:GBK
	public String sign;// ǩ��
	public String sign_key_index;// ����Կ֧�ֵ���Կ��ţ�Ĭ��1

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getService_version() {
		return service_version;
	}

	public void setService_version(String service_version) {
		this.service_version = service_version;
	}

	public String getInput_charset() {
		return input_charset;
	}

	public void setInput_charset(String input_charset) {
		this.input_charset = input_charset;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign_key_index() {
		return sign_key_index;
	}

	public void setSign_key_index(String sign_key_index) {
		this.sign_key_index = sign_key_index;
	}

}
