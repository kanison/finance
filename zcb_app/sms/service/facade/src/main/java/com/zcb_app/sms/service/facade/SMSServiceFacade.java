package com.zcb_app.sms.service.facade;

import com.zcb_app.sms.service.facade.dataobject.SendCodeParams;
import com.zcb_app.sms.service.facade.dataobject.SendMessageParams;
import com.zcb_app.sms.service.facade.dataobject.VerifyCodeParams;

public interface SMSServiceFacade {
	
	/**
	 * �·�������֤��<br>
	 * 1��	�������������ֻ��Ÿ�ʽ�Ƿ���ȷ��ģ���Ƿ�����<br>
	 * 2��	���ֻ��ź�IP����Ƿ񳬹�Ƶ�����ƣ����Կ���ʹ��memcached��mysql�ڴ����������ȷ�ʽ������Ƶ�ʣ�<br>
	 * 3��	������֤��<br>
	 * 4��	������֤�룬����ʷ����֤����Ϣ��¼��ˮ����<br>
	 * 5��	����ģ���������������ɶ�������<br>
	 * 6��	�����������·�����<br>
	 * @param params �������
	 * @Return void
	 * @author Gu.Dongying 
	 * @Date 2015��4��30�� ����2:17:12
	 */
	public void sendMCode(SendCodeParams params);
	
	/**
	 * ��֤������֤��<br>
	 * 1��	�������������ֻ��Ÿ�ʽ�Ƿ���ȷ��ģ���Ƿ�����<br>
	 * 2��	��ѯ��ǰ�ֻ��ź�ģ��ID��Ӧ������������Ϣ<br>
	 * 3��	У���Ƿ񳬹���Ч�ڣ�У���Ƿ񳬹���֤����<br>
	 * 4��	У���������֤�����·�����֤���Ƿ���ͬ����������֤��Ӧ�Ľ����Ϣ<br>
	 * @param params �������
	 * @return ������֤����͹�����Ϣ
	 * @Return String
	 * @author Gu.Dongying 
	 * @Date 2015��4��30�� ����2:19:19
	 */
	public String verifyMCode(VerifyCodeParams params);
	
	/**
	 * 7.1.3 ����֪ͨ����<br>
	 * 1��	���op_code�Ƿ���ȷ<br>
	 * 2��	�������������ֻ��Ÿ�ʽ�Ƿ���ȷ��ģ���Ƿ�����<br>
	 * 3��	��֤IP�Ƿ�Ϊ�ڲ���ȨIP���·�֪ͨ�����Ϣֻ���ڲ��������Ļ������Ե��ã������ٽ���Ƶ�����ƿ��ơ�<br>
	 * 4��	���������Ϣ����tmpl_value��ֵ���浽Frela_info�ֶ��У�������ʷ�Ķ��ŷ�����Ϣ��¼��ˮ����<br>
	 * 5��	����ģ���������������ɶ�������<br>
	 * 6��	�����������·�����
	 * @param params
	 * @author Gu.Dongying 
	 * @Date 2015��5��4�� ����9:33:53
	 */
	public void sendSM(SendMessageParams params);
	
}
