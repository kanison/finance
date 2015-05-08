package com.zcb_app.sms.service.dao.type;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;

import com.zcb_app.sms.service.facade.dataobject.SendMessageParams;
import com.zcb_app.sms.service.utils.SMSServiceCommonConstant;

public class MsgSendMessageParams {

	private String mobile_no;//手机号
	
	private BigInteger tmpl_id;//短信模板ID
	
	private String client_ip;//用户IP
	
	private Map<String, String> tmpl_value;//短信模板的参数值，以key=value的形式传递
	
	private String tmpl_value_str;
	
	public String getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
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

	public Map<String, String> getTmpl_value() {
		return tmpl_value;
	}

	public void setTmpl_value(Map<String, String> tmpl_value) {
		this.tmpl_value = tmpl_value;
		if(this.tmpl_value != null && !this.tmpl_value.isEmpty()){
			if(tmpl_value.keySet() != null && !tmpl_value.keySet().isEmpty()){
				StringBuilder sb = new StringBuilder();
				Iterator<String> iterator = tmpl_value.keySet().iterator();
				while(iterator.hasNext()){
					sb.append(iterator.next());
					sb.append(SMSServiceCommonConstant.EQUALITY_SIGN);
					sb.append(this.tmpl_value.get(iterator.next()));
				}
			}
		}
	}

	public String getTmpl_value_str() {
		return tmpl_value_str;
	}

	public void setTmpl_value_str(String tmpl_value_str) {
		this.tmpl_value_str = tmpl_value_str;
	}

	public static MsgSendMessageParams valueOf(SendMessageParams params){
		MsgSendMessageParams smsgParams = new MsgSendMessageParams();
		smsgParams.setClient_ip(params.getClient_ip());
		smsgParams.setMobile_no(params.getMobile());
		smsgParams.setTmpl_id(params.getTmpl_id());
		smsgParams.setTmpl_value(params.getTmpl_value());
		return smsgParams;
	}
}
