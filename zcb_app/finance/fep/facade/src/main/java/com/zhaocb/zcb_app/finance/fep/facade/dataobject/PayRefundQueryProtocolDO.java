package com.zhaocb.zcb_app.finance.fep.facade.dataobject;

import java.io.Serializable;

/**
 * 财付通退票查询协议参数
 * 
 * @author zhl
 *
 */
public class PayRefundQueryProtocolDO implements Serializable {

	private static final long serialVersionUID = 5443396857016093996L;

	public String sign_type;// 签名类型,取值：MD5、RSA，默认：MD5
	public String service_version;// 版本号,默认为1.0
	public String input_charset;// 字符编码,取值：GBK、UTF-8,默认:GBK
	public String sign;// 签名
	public String sign_key_index;// 多密钥支持的密钥序号，默认1

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
