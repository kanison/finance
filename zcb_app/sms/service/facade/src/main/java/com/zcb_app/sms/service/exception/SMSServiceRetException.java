package com.zcb_app.sms.service.exception;

import com.tenpay.sm.lang.error.ErrorCodeException;

public class SMSServiceRetException  extends ErrorCodeException{
	
	private static final long serialVersionUID = 954043913218832333L;
	
	public static final String SYSTEM_ERROR="30010000";//ϵͳ����
	
	public static final String ERR_MOBILE_PARAMS = "30010001";//�ֻ����������
	
	public static final String ERR_TEMPLATE_PARAMS = "30010002";//����ģ�����
	
	public static final String ERR_CLIENT_IP_PARAMS = "30010003";//�û�IP����
	
	public static final String ERR_VERIFY_CODE_PARAMS = "30010004";//��֤�����
	
	public static final String ERR_OPERATION_CODE_PARAMS = "30010005";//����֪ͨ��Ϣ���������
	
	public static final String ERR_SEND_CODE_PARAMS = "30010006";//�·�������֤���������!
	
	public static final String ERR_IP_INTERNAL_AUTHORIZATION = "30010007";//�ͻ���IP�ǲ���ȨIP!
	
	public static final String ERR_VERIFY_CODE = "30010008";//��֤�벻ƥ�䣡
	
	public static final String ERR_VERIFY_CODE_EXPIRED = "30010010";//��֤���ѹ��ڣ�
	
	public static final String ERR_VERIFY_CODE_TIMES = "30010011";//��֤��У�鳬��У�������
	
	public static final String ERR_EFFECTED_ROWS = "30010009";//���µ�Ӱ��������Ϊ1
	
	public static final String ERR_EXCEED_MOBILE_NO_LIMIT = "300100012";//�ֻ��ų���Ƶ������
	
	public static final String ERR_EXCEED_IP_LIMIT = "300100013";//IP��ַ����Ƶ������
	
	public SMSServiceRetException(String msg) {
		super(msg);
	}

	public SMSServiceRetException(String msg, Exception e) {
		super(msg, e);
	}

	public SMSServiceRetException(Exception e) {
		super(e);
	}
	
	public SMSServiceRetException(String retcode, String retmsg) {
		
		super(retcode, retmsg);
	}
}
