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
	public static final String INPUT_PARAMS_ERROR="20020001";//��������
	public static final String USER_EXIST="20020002";//�û��Ѵ���
	public static final String USER_NOT_EXIST="20020003";//�û�δע��
	public static final String PASSWORD_ERROR="20020004";//�������
	public static final String BALANCE_NOT_ENOUGH="20020005";//�˻�����
	public static final String FREEZE_BALANCE_NOT_ENOUGH="20020006";//�˻���������
	public static final String LISTID_NOT_EXIST="20020007";//���Ų�����
	public static final String LISTID_FREEZE_ERROR="20020008";//���ᵥ����
	public static final String FREEZE_ID_ERROR="20020009";//�ⶳ���Ŵ���
	public static final String FREEZE_BALANCE_ERROR="20020010";//�ⶳ������


	
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
