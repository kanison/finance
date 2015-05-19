package com.zcb_app.sms.service.dao;

import com.zcb_app.sms.service.dao.type.MsgSendCodeParams;
import com.zcb_app.sms.service.dao.type.MsgSendMessageParams;
import com.zcb_app.sms.service.facade.dataobject.IPLimitDO;
import com.zcb_app.sms.service.facade.dataobject.MobileLimitDO;
import com.zcb_app.sms.service.facade.dataobject.SendInfoDO;
import com.zcb_app.sms.service.facade.dataobject.VerifyCodeInfoDO;

/**
 * @author Gu.Dongying
 */
public interface SMSServiceDAO {
	
	/**
	 * ����ҵ��<br>
	 * 1�������·�����֤��<br>
	 * 2������ʷ����֤����Ϣ��¼��ˮ����<br>
	 * @param verifyCode
	 * @author Gu.Dongying 
	 * @Date 2015��5��4�� ����2:52:30
	 */
	public void saveVerifyCodeInfo(MsgSendCodeParams verifyCode);
	
	/**
	 * ��ѯ�ֻ���Ƶ�����޼�¼
	 * @param mobileLimit �ֻ��š�ģ��ID
	 * @return MobileLimitDO
	 * @author Gu.Dongying 
	 * @Date 2015��5��5�� ����9:43:14
	 */
	public MobileLimitDO queryMobileLimit(MobileLimitDO mobileLimit);
	
	/**
	 * ��ѯIPƵ�����޼�¼
	 * @param ipLimit IP��ģ��ID
	 * @return IPLimitDO
	 * @author Gu.Dongying 
	 * @Date 2015��5��5�� ����9:43:14
	 */
	public IPLimitDO queryIPLimit(IPLimitDO ipLimit);

	/**
	 * ���������Ϣ
	 * @param params
	 * @author Gu.Dongying 
	 * @Date 2015��5��5�� ����10:55:03
	 */
	public void saveMsgInfo(MsgSendMessageParams params);
	
	/**
	 * ��ѯ��ǰ�ֻ��ź�ģ��ID��Ӧ������������Ϣ(ȡ���һ�������ļ�¼)
	 * @param codeInfo ��ǰ�ֻ��ź�ģ��ID
	 * @return VerifyCodeInfoDO
	 * @author Gu.Dongying 
	 * @Date 2015��5��5�� ����11:45:52
	 */
	public VerifyCodeInfoDO queryMsgInfo(VerifyCodeInfoDO codeInfo);
	
	/**
	 * ������֤��Ӧ�Ľ����Ϣ
	 * @param codeInfo ��ǰ�ֻ��ź�ģ��ID
	 * @author Gu.Dongying 
	 * @Date 2015��5��5�� ����12:57:41
	 */
	public void updateVerifyCodeInfo(VerifyCodeInfoDO codeInfo);
	
	/**
	 * �������Ͷ�����Ϣ��¼
	 * @param sendInfo
	 * @author Gu.Dongying 
	 * @Date 2015��5��5�� ����5:40:51
	 */
	public void addSendInfo(SendInfoDO sendInfo);
	
	/**
	 * ����ֻ���Ƶ�����Ƽ�¼
	 * @param mobileLimit �ֻ���Ƶ��������Ϣ
	 * @author Gu.Dongying 
	 * @Date 2015��5��6�� ����9:25:01
	 */
	public void addMobileLimitInfo(MobileLimitDO mobileLimit);
	
	/**
	 * ���IP��ַƵ��������Ϣ��¼
	 * @param ipLimit IP��ַƵ��������Ϣ
	 * @author Gu.Dongying 
	 * @Date 2015��5��6�� ����9:26:08
	 */
	public void addIPLimitInfo(IPLimitDO ipLimit);
	
	/**
	 * 1���ų��Ƿ��ֻ��ź�IP�ڲ���Ƶ�����Ƶİ�������<br>
	 * 2�����ֻ��ź�IP����Ƿ񳬹�Ƶ�����ƣ�������Ƶ����Ϣ
	 * 
	 * @param sParams
	 *            code:�ֻ��� clientIP:�ֻ��Ŷ�Ӧ�Ŀͻ���IP
	 * @author Gu.Dongying
	 * @Date 2015��4��30�� ����3:08:07
	 */
	public void ctrlSendCodeLimit(MsgSendCodeParams sParams);
	
}
