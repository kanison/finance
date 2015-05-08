package com.zcb_app.sms.service.dao.ibatis;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.zcb_app.sms.service.dao.SMSServiceDAO;
import com.zcb_app.sms.service.dao.type.MsgSendCodeParams;
import com.zcb_app.sms.service.dao.type.MsgSendMessageParams;
import com.zcb_app.sms.service.exception.SMSServiceRetException;
import com.zcb_app.sms.service.facade.dataobject.IPLimitDO;
import com.zcb_app.sms.service.facade.dataobject.MobileLimitDO;
import com.zcb_app.sms.service.facade.dataobject.MsgLogDO;
import com.zcb_app.sms.service.facade.dataobject.MsgSettingsDO;
import com.zcb_app.sms.service.facade.dataobject.SendInfoDO;
import com.zcb_app.sms.service.facade.dataobject.StrategyDO;
import com.zcb_app.sms.service.facade.dataobject.VerifyCodeInfoDO;
import com.zcb_app.sms.service.utils.SMSServiceTemplateUtils;


public class SMSServiceIbatisImpl extends SqlMapClientDaoSupport implements
		SMSServiceDAO {
	private static final Log LOG = LogFactory.getLog(SMSServiceIbatisImpl.class);
	/**
	 * �����·�����֤��
	 * @param verifyCode
	 * @author Gu.Dongying 
	 * @Date 2015��5��4�� ����2:52:30
	 */
	private void createVerifyCode(VerifyCodeInfoDO verifyCode){
		if(verifyCode == null){
			throw new SMSServiceRetException(SMSServiceRetException.ERR_SEND_CODE_PARAMS, "�·�������֤���������!");
		}
		verifyCode.genDataTableIndex();
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertVerifyCode", verifyCode);
	}

	/** 
	 * ��¼�·���֤�����ˮ��־
	 * @author Gu.Dongying 
	 * @Date 2015��5��4�� ����4:29:35
	 */
	private void createMsgLog(MsgLogDO msgLog) {
		if(msgLog == null){
			throw new SMSServiceRetException(SMSServiceRetException.ERR_SEND_CODE_PARAMS, "��¼������ˮ����!");
		}
		msgLog.genDataTableIndex();
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertMsgLog", msgLog);
	}

	/** 
	 * ����ҵ��<br>
	 * 1�������·�����֤��<br>
	 * 2������ʷ����֤����Ϣ��¼��ˮ����<br>
	 * @see com.zcb_app.mvcode.service.dao.SMSServiceDAO#saveVerifyCodeInfo(com.zcb_app.mvcode.service.facade.dataobject.VerifyCodeInfoDO)
	 * @author Gu.Dongying 
	 * @Date 2015��5��4�� ����4:33:32
	 */
	@Transactional
	public void saveVerifyCodeInfo(MsgSendCodeParams params) {
		
		VerifyCodeInfoDO verifyCode = new VerifyCodeInfoDO();
		//��ѯ��ԭ����֤����Ϣ
		verifyCode.setFmobile_no(params.getMobile());
		verifyCode.setFtmpl_id(params.getTmpl_id());
		VerifyCodeInfoDO srcVerifyCode = queryVerifyCodeInfo(verifyCode);
		//�����·�����֤��
		verifyCode = new VerifyCodeInfoDO();
		verifyCode.setFcheck_time(params.getCheck_time());
		verifyCode.setFchk_err_times(params.getChk_err_times());
		verifyCode.setFchk_suc_times(params.getChk_suc_times());
		verifyCode.setFclient_ip(params.getClient_ip());
		verifyCode.setI_expired_time(params.getExpired_time());
		verifyCode.setFmobile_no(params.getMobile());
		verifyCode.setFrela_info(params.getRelation_info());
		verifyCode.setFrela_key(params.getRelation_key());
		verifyCode.setFsend_time(params.getSend_time());
		verifyCode.setFtmpl_id(params.getTmpl_id());
		verifyCode.setFverify_code(params.getVerify_code());
		createVerifyCode(verifyCode);
		if(srcVerifyCode != null){			
			//����ʷ����֤����Ϣ��¼��ˮ���� 
			MsgLogDO msgLog = new MsgLogDO();
			msgLog.setFcheck_time(srcVerifyCode.getFcheck_time());
			msgLog.setFchk_err_times(srcVerifyCode.getFchk_err_times());
			msgLog.setFchk_suc_times(srcVerifyCode.getFchk_suc_times());
			msgLog.setFclient_ip(srcVerifyCode.getFclient_ip());
			msgLog.setFexpired_time(srcVerifyCode.getFexpired_time());
			msgLog.setFmobile_no(srcVerifyCode.getFmobile_no());
			msgLog.setFrela_info(srcVerifyCode.getFrela_info());
			msgLog.setFrela_key(srcVerifyCode.getFrela_key());
			msgLog.setFsend_time(srcVerifyCode.getFsend_time());
			msgLog.setFtmpl_id(srcVerifyCode.getFtmpl_id());
			msgLog.setFverify_code(srcVerifyCode.getFverify_code());
			createMsgLog(msgLog);
		}
	}
	
	/**
	 * ��ѯ�ֻ���Ƶ�����޼�¼
	 * @param mobileLimit �ֻ��š�ģ��ID
	 * @return MobileLimitDO
	 * @author Gu.Dongying 
	 * @Date 2015��5��5�� ����9:43:14
	 */
	public MobileLimitDO queryMobileLimit(MobileLimitDO mobileLimit){
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		MobileLimitDO mobile = (MobileLimitDO)client.queryForObject("queryMobileLimit", mobileLimit);
		return mobile;
	}
	
	/**
	 * ��ѯIPƵ�����޼�¼
	 * @param ipLimit IP��ģ��ID
	 * @return IPLimitDO
	 * @author Gu.Dongying 
	 * @Date 2015��5��5�� ����9:43:14
	 */
	public IPLimitDO queryIPLimit(IPLimitDO ipLimit){
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		IPLimitDO ip = (IPLimitDO)client.queryForObject("queryIPLimit", ipLimit);
		return ip;
	}

	/** 
	 * ��¼������Ϣ��ˮ
	 * @see com.zcb_app.mvcode.service.dao.SMSServiceDAO#saveMsgInfo(com.zcb_app.mvcode.service.dao.type.MsgSendMessageParams)
	 * @author Gu.Dongying 
	 * @Date 2015��5��5�� ����11:02:01
	 */
	@Transactional
	public void saveMsgInfo(MsgSendMessageParams params) {
		MsgLogDO msgLog = new MsgLogDO();
		msgLog.setFclient_ip(params.getClient_ip());
		msgLog.setFmobile_no(params.getMobile_no());
		msgLog.setFrela_info(params.getTmpl_value_str());
		msgLog.setFtmpl_id(params.getTmpl_id());
		createMsgLog(msgLog);
	}
	
	/**
	 * ��ѯ��ǰ�ֻ��ź�ģ��ID��Ӧ������������Ϣ
	 * @param codeInfo ��ǰ�ֻ��ź�ģ��ID
	 * @return VerifyCodeInfoDO
	 * @author Gu.Dongying 
	 * @Date 2015��5��5�� ����11:45:52
	 */
	public VerifyCodeInfoDO queryVerifyCodeInfo(VerifyCodeInfoDO codeInfo){
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		codeInfo.genDataTableIndex();
		VerifyCodeInfoDO vCodeInfo = (VerifyCodeInfoDO)client.queryForObject("queryVerifyCodeInfo", codeInfo);
		return vCodeInfo;
	}
	
	/**
	 * ������֤��Ӧ�Ľ����Ϣ
	 * @param codeInfo ��ǰ�ֻ��ź�ģ��ID
	 * @author Gu.Dongying 
	 * @Date 2015��5��5�� ����12:57:41
	 */
	@Transactional
	public void updateVerifyCodeInfo(VerifyCodeInfoDO codeInfo){
		//��ס��������
		codeInfo.setQuerylock(true);
		queryVerifyCodeInfo(codeInfo);
		
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		int effected_rows = client.update("updateVerifyCodeInfo", codeInfo);
		if (1 != effected_rows) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_EFFECTED_ROWS, "���µ�Ӱ��������Ϊ1");
		}
	}

	/** 
	 * �������Ͷ�����Ϣ��¼
	 * @see com.zcb_app.mvcode.service.dao.SMSServiceDAO#addSendInfo(com.zcb_app.mvcode.service.facade.dataobject.SendInfoDO)
	 * @author Gu.Dongying 
	 * @Date 2015��5��5�� ����5:41:18
	 */
	@Transactional
	public void addSendInfo(SendInfoDO sendInfo) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertSendInfo", sendInfo);
	}
	
	/**
	 * ����ֻ���Ƶ�����Ƽ�¼
	 * @param mobileLimit �ֻ���Ƶ��������Ϣ
	 * @author Gu.Dongying 
	 * @Date 2015��5��6�� ����9:25:01
	 */
	@Transactional
	public void addMobileLimitInfo(MobileLimitDO mobileLimit){
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertMobileLimit", mobileLimit);
	}
	
	/**
	 * ���IP��ַƵ��������Ϣ��¼
	 * @param ipLimit IP��ַƵ��������Ϣ
	 * @author Gu.Dongying 
	 * @Date 2015��5��6�� ����9:26:08
	 */
	@Transactional
	public void addIPLimitInfo(IPLimitDO ipLimit){
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertIPLimit", ipLimit);
	}

	/** 
	 * ���·�����֤��Ƶ�����Ƶ���Ϣ
	 * @see com.zcb_app.mvcode.service.dao.SMSServiceDAO#modifySendCodeLimitInfo(com.zcb_app.mvcode.service.dao.type.MsgSendCodeParams)
	 * @author Gu.Dongying 
	 * @Date 2015��5��6�� ����10:37:25
	 */
	@Transactional
	public void modifySendCodeLimitInfo(MsgSendCodeParams scParams) {
		MsgSettingsDO settings = SMSServiceTemplateUtils.readMsgSettings();
		List<StrategyDO> strategys = settings.getStrategys();
		if (strategys != null && !strategys.isEmpty()) {
			// �ֻ���Ƶ��
			SendInfoDO mobileSendInfo = new SendInfoDO();
			mobileSendInfo.setFmobile_no(scParams.getMobile());
			mobileSendInfo.setFtmpl_id(scParams.getTmpl_id());
			// IPƵ��
			SendInfoDO ipSendInfo = new SendInfoDO();
			ipSendInfo.setFclient_ip(scParams.getClient_ip());
			ipSendInfo.setFtmpl_id(scParams.getTmpl_id());

			MobileLimitDO mobileLimit;
			IPLimitDO ipLimit;
			for (StrategyDO strategy : strategys) {
				if (strategy != null) {
					mobileSendInfo.setTimespan(strategy.getTimespan());
					// �ﵽ�ֻ���Ƶ�����޷�ֵ��������޼�¼
					if (querySendCodeInfoCount(mobileSendInfo) >= strategy.getMob_no_limit()) {
						try {
							mobileLimit = new MobileLimitDO();
							mobileLimit.setFblock_timespan(strategy.getTimespan());
							mobileLimit.setFmobile_no(scParams.getMobile());
							mobileLimit.setFtmpl_id(scParams.getTmpl_id());
							addMobileLimitInfo(mobileLimit);
						} catch (Exception e) {
							LOG.error("����ֻ�������Ƶ����Ϣʧ�ܣ�Mobile��" + scParams.getMobile() + "��TemplateID:"
									+ scParams.getTmpl_id() + "��");
						}
					}

					ipSendInfo.setTimespan(strategy.getTimespan());
					// �ﵽIPƵ�����޷�ֵ��������޼�¼
					if (querySendCodeInfoCount(ipSendInfo) >= strategy.getIp_limit()) {
						try {
							ipLimit = new IPLimitDO();
							ipLimit.setFblock_timespan(strategy.getTimespan());
							ipLimit.setFclient_ip(scParams.getClient_ip());
							ipLimit.setFtmpl_id(scParams.getTmpl_id());
							addIPLimitInfo(ipLimit);
						} catch (Exception e) {
							LOG.error("���IP����Ƶ����Ϣʧ�ܣ�IP��" + scParams.getMobile() + "��TemplateID:"
									+ scParams.getTmpl_id() + "��");
						}
					}
				}
			}
		}
		
	}
	
	private int querySendCodeInfoCount(SendInfoDO sendInfo){
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		sendInfo.genDataTableIndex();
		int count = (Integer)client.queryForObject("querySendCodeInfoCount", sendInfo);
		return count;
	}
	
}
