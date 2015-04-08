package com.zhaocb.app.website.web.exception;

import com.tenpay.sm.lang.error.ErrorCodeException;

public class FundMchapiWebRetException  extends ErrorCodeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4660797621140148970L;
	public static final String SYSTEM_ERROR="87710000";//ϵͳ����
	public static final String FILE_NOT_EXIST ="87720303";//�ļ�������
	
	
	
	public static final String PARSE_PARAM_ERROR="87710001";//���������쳣
	
	public static final String NO_OR_INCORRECT_ID_CARD="87720202";//�����֤�����ݲ�֧�ֿ�ͨ�����ף�����������Ƹ�ͨ�ͷ���ϵ
	public static final String NO_FUND_ACCOUNT="87720203";//δ��ͨ������˻�
	public static final String ERR_UNKOWN_FUNDCOR="87720204";//ָ������˾��δ�ͲƸ�ͨǩ��Э��
	public static final String EXIST_CONTRACT_FUNDCOR="87720205";//�Ѿ���û���˾ǩԼ
	public static final String CALL_PL_PAY_EXCEPTION="87720206";//��̨֧��cgi����-1��ת�崦��
	public static final String LOGIN_ERROR="87720200";//��¼�쳣
	public static final String NOT_TRUE_NAME_USER="87720207";//ʵ�����δͨ��
	public static final String VERIFY_CODE_ERROR="87720208";//�ֻ���֤�벻��ȷ
	public static final String NO_FUND_MOBILE="87720209";  //û�в�ѯ�������˻��󶨵��ֻ�����
	public static final String NO_BAND_CARD="87720210";  //û�а����п�
	public static final String ERR_CREID="87720211";  //���֤��Ϣ��֤����
	public static final String ERR_OLD_MOBILE="87720212";  //ԭ�ֻ�������֤����
	public static final String SEND_PACK_MOBILE_ERROR="87720213";  //�����ֻ���֤���������
	public static final String NO_FUND_DRAW_LIST="87720214";  //û�����ּ�¼
	public static final String NO_FUND_ACCOUNT_INFO="87720215";  //û�в鵽�����û��˻���Ϣ  ������Ϣ
	public static final String NO_FUND_ACCOUNT_DETAIL="87720216";  //û���˻���ϸ��Ϣ
	public static final String NO_SUPPORT_FUND_CORP="87720217";  //û���˻���ϸ��Ϣ
	public static final String ERR_SUPPORT_FUND_CORP="87720218";  //����˾��Ϣ������
	public static final String UNKONWN_SENDSCENE="87720219";  //δ֪���Ͷ�����֤�볡��
	public static final String NO_TRANS_LIST="87720220";  //û�н��׼�¼
	public static final String ERR_MODIFY_MOBILE="87720221";  //�����ֻ��ų���
	public static final String ERR_SAME_MOBILE="87720222";  //���ֻ��Ѿ������ĲƸ�ͨ��,���������ֻ�����
	public static final String ERR_REP_MBCODE_NOTIME ="87720223"; //��֤�뷢��Ƶ�������Ժ����µ������
	public static final String USER_NOT_AGREE_SHARE="87720224";  //�û�δͬ��Ƹ�ͨ�ӻ���˾��ȡ�ֲ���Ϣ
	public static final String ERR_MOBILE_VERIFIED_EXPR_TIME="87720225";  //��������֤���ֻ�����
	public static final String MOBILE_USER_CANNOT_MODIFY="87720226";  //�ֻ��ŵĲƸ�ͨ�û��������޸��ֻ�����
	public static final String USER_TYPE_ERROR="87720227";  //�û����ʹ���
	public static final String PAY_PASSWD_ERROR="87720228";  //���������ע��������ĸ��ϼ���Сд��
	public static final String NOT_REGISTER_CFT="87720229";  //û�м���Ƹ�ͨ
	
	public static final String ERR_ACCOUNT_INFO="87720009";//������Ϣ��������ʻ���Ϣ��һ��
	public static final String ERR_OPEN_FUND_ACCOUNT="87720008";//�Ѿ���ͨ�������ʻ�
	public static final String ERR_CRE_OPEN_FUND_ACCOUNT="87720010";//�����֤���û��Ѿ���ͨ�������ʻ�
	public static final String ERR_EXIST_BIND_CARD ="20021"; //�Ѱ󶨹������ʺţ������ٴΰ�
	
	public static final String CAN_NOT_UPLOAD_FILE ="87720300"; //�ϴ�����������ѳ����ļ����ϴ�ʱ��
	public static final String QUERY_CONTRACT_ERROR ="87720301";//��ѯǩԼ��Ϣʧ��
	public static final String TRAN_ID_NOT_EXIST ="87720302";//����������
	
	public static final String ORDER_BUY_FAIL ="87720304";//����ʧ��
	
	public static final String NOT_BIND_FUND_SP = "87720305";  //û�а󶨸û���˾
	public static final String TRANS_STATUS_ERROR = "87720306";  //����״̬����7״̬��
	public static final String SP_ACCOUNT_STATUS_ERROR = "87720307";  //�󶨵Ļ���˾�˻��쳣
	public static final String NOT_FIND_TRANS_ORDER ="87720308"; //û�в�ѯ���ö���
	public static final String MOBILE_ERROR ="87720309"; //�ֻ�������Ƹ�ͨ�󶨵Ĳ�һ��
	public static final String NOT_AUTHED_USER ="87720310";  //��ʵ����֤�û�
	public FundMchapiWebRetException(String msg) {
		super(msg);
	}

	public FundMchapiWebRetException(String msg, Exception e) {
		super(msg, e);
	}

	public FundMchapiWebRetException(Exception e) {
		super(e);
	}
	
	public FundMchapiWebRetException(String retcode, String retmsg) {
		super(retcode, retmsg);
	}
}
