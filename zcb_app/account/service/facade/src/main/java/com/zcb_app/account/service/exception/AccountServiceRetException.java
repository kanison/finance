package com.zcb_app.account.service.exception;

import com.tenpay.sm.lang.error.ErrorCodeException;


public class AccountServiceRetException  extends ErrorCodeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 954043913218832333L;
	/**
	 * ������ǰ3λ��ģ��������ģ��Ϊ200��
	 * ����λ1��ʾϵͳ����2��ʾҵ�����
	 * ����λΪ�������
	 */
	public static final String SYSTEM_ERROR="20010000";//ϵͳ����
	public static final String PARSE_PARAM_ERROR="20010001";//���������쳣
	public static final String ERR_DATA_SIGN_ABN = "20010002";//����ǩ���쳣
	public static final String ERR_EFFECTED_ROWS = "20010003";//���µ�Ӱ��������Ϊ1
	
	//ҵ�������20029001~20029999Ϊͳһ��ҵ�������
	public static final String INPUT_PARAMS_ERROR="20029001";//��������
	public static final String ERR_REENTY_INCONSISTENT = "20029002";//���������һ��
	public static final String ERR_REENTY_OK = "20029003";//��������룬����һ��
	
	public static final String USER_EXIST="20020001";//�û��Ѵ���
	public static final String USER_NOT_EXIST="20020002";//�û�δע��
	public static final String PASSWORD_ERROR="20020003";//�������
	public static final String BALANCE_NOT_ENOUGH="20020004";//�˻�����
	public static final String FREEZE_BALANCE_NOT_ENOUGH="20020005";//�˻���������
	public static final String LISTID_NOT_EXIST="20020006";//���Ų�����
	public static final String LISTID_FREEZE_ERROR="20020007";//���ᵥ����
	public static final String FREEZE_ID_ERROR="20020008";//�ⶳ���Ŵ���
	public static final String FREEZE_BALANCE_ERROR="2002009";//�ⶳ������
	public static final String FREEZE_STATUS_ERROR="2002010";//ԭ���ᵥ״̬����
		
	public static final String ERR_USER_NOT_EXSIT = "20020010";//�û�������
	public static final String ERR_USER_ACCT_NOT_EXSIT = "20020011";//�û��ֽ��˺Ų�����
	
	public AccountServiceRetException(String msg) {
		super(msg);
	}

	public AccountServiceRetException(String msg, Exception e) {
		super(msg, e);
	}

	public AccountServiceRetException(Exception e) {
		super(e);
	}
	
	public AccountServiceRetException(String retcode, String retmsg) {
		
		super(retcode, retmsg);
	}
}
