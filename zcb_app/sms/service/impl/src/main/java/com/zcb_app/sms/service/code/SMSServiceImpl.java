package com.zcb_app.sms.service.code;

import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import com.zcb_app.sms.service.dao.SMSServiceDAO;
import com.zcb_app.sms.service.dao.type.MsgSendCodeParams;
import com.zcb_app.sms.service.dao.type.MsgSendMessageParams;
import com.zcb_app.sms.service.dao.type.MsgVerifyCodeParams;
import com.zcb_app.sms.service.exception.SMSServiceRetException;
import com.zcb_app.sms.service.facade.SMSServiceFacade;
import com.zcb_app.sms.service.facade.dataobject.IPLimitDO;
import com.zcb_app.sms.service.facade.dataobject.MobileLimitDO;
import com.zcb_app.sms.service.facade.dataobject.MsgSettingsDO;
import com.zcb_app.sms.service.facade.dataobject.MsgTemplateDO;
import com.zcb_app.sms.service.facade.dataobject.SendCodeParams;
import com.zcb_app.sms.service.facade.dataobject.SendInfoDO;
import com.zcb_app.sms.service.facade.dataobject.SendMessageParams;
import com.zcb_app.sms.service.facade.dataobject.VerifyCodeInfoDO;
import com.zcb_app.sms.service.facade.dataobject.VerifyCodeParams;
import com.zcb_app.sms.service.utils.SMSServiceCommonConstant;
import com.zcb_app.sms.service.utils.SMSServiceTemplateUtils;
import com.zcb_app.sms.service.utils.VerifyCodeUtils;

/**
 * ���ŷ�����ʵ����
 * @author Gu.Dongying
 *
 */
public class SMSServiceImpl implements SMSServiceFacade {

	// ����֪ͨ���Ų�����
	private static final String SEND_MSG_OP_CODE = "100010003";

	private static final String PLACE_AUTH_CODE = "auth_code";

	/**
	 * ���ŷ���������
	 */
	private SMSServiceDAO smsServiceDAO;

	/**
	 * ��ȡ���ŷ���������
	 * 
	 * @return smsServiceDAO ���ŷ���������
	 */
	public SMSServiceDAO getSmsServiceDAO() {
		return smsServiceDAO;
	}

	/**
	 * ���ö��ŷ���������
	 * 
	 * @param smsServiceDAO
	 *            the ���ŷ��������� to set
	 */
	public void setSmsServiceDAO(SMSServiceDAO smsServiceDAO) {
		this.smsServiceDAO = smsServiceDAO;
	}

	/**
	 * �·�������֤��<br>
	 * 1���������������ֻ��Ÿ�ʽ�Ƿ���ȷ��ģ���Ƿ�����<br>
	 * 2�����ֻ��ź�IP����Ƿ񳬹�Ƶ������<br>
	 * 3��������֤��<br>
	 * 4��������֤�룬����ʷ����֤����Ϣ��¼��ˮ����<br>
	 * 5������ģ���������������ɶ�������<br>
	 * 6�������������·�����<br>
	 * 
	 * @param params
	 *            �������
	 * @author Gu.Dongying
	 * @Date 2015��4��30�� ����2:17:12
	 */
	public void sendMCode(SendCodeParams params) {
		// 1���������������ֻ��Ÿ�ʽ�Ƿ���ȷ��ģ���Ƿ�����
		checkSendInParameters(params);
		checkMobilephoneCode(params.getMobile());
		checkTemplate(params.getTmpl_id());
		MsgSendCodeParams sParams = MsgSendCodeParams.valueOf(params);
		// 2�����ֻ��ź�IP����Ƿ񳬹�Ƶ������
		ctrlSendCodeLimit(sParams);

		// 3��������֤��;4��������֤�룬����ʷ����֤����Ϣ��¼��ˮ����
		MsgTemplateDO template = SMSServiceTemplateUtils.getTemplate(params.getTmpl_id());
		sParams.setExpired_time(template.getExpire_time());
		String verifyCode = operationVerifyCode(sParams, template.getAuth_code_len());

		// ��¼�·��Ķ��ż�¼
		SendInfoDO sendInfo = new SendInfoDO();
		sendInfo.setFclient_ip(sParams.getClient_ip());
		sendInfo.setFmobile_no(sParams.getMobile());
		sendInfo.setFtmpl_id(sParams.getTmpl_id());
		smsServiceDAO.addSendInfo(sendInfo);

		Map<String, String> tempValues = params.getTmpl_value();
		tempValues.put(PLACE_AUTH_CODE, verifyCode);
		// 5������ģ���������������ɶ�������
		String msg = generateMsg(template.getTmpl_text(), tempValues);
		// 6�������������·�����
		sendMsg(params.getMobile(), msg);
	}

	/**
	 * ��֤������֤��<br>
	 * 1���������������ֻ��Ÿ�ʽ�Ƿ���ȷ��ģ���Ƿ�����<br>
	 * 2����ѯ��ǰ�ֻ��ź�ģ��ID��Ӧ������������Ϣ<br>
	 * 3��У���Ƿ񳬹���Ч�ڣ�У���Ƿ񳬹���֤����<br>
	 * 4��У���������֤�����·�����֤���Ƿ���ͬ����������֤��Ӧ�Ľ����Ϣ<br>
	 * 
	 * @param params
	 *            �������
	 * @return String ������֤����͹�����Ϣ
	 * @author Gu.Dongying
	 * @Date 2015��4��30�� ����2:19:19
	 */
	public String verifyMCode(VerifyCodeParams params) {
		// �������������ֻ��Ÿ�ʽ�Ƿ���ȷ��ģ���Ƿ�����
		checkVerifyInParameters(params);
		checkMobilephoneCode(params.getMobile());
		checkTemplate(params.getTmpl_id());
		// У����֤��
		MsgVerifyCodeParams vParams = MsgVerifyCodeParams.valueOf(params);
		return checkVerifyCode(vParams);
	}

	/**
	 * 7.1.3 ����֪ͨ����<br>
	 * 1�����op_code�Ƿ���ȷ<br>
	 * 2���������������ֻ��Ÿ�ʽ�Ƿ���ȷ��ģ���Ƿ�����<br>
	 * 3����֤IP�Ƿ�Ϊ�ڲ���ȨIP���·�֪ͨ�����Ϣֻ���ڲ��������Ļ������Ե��ã������ٽ���Ƶ�����ƿ��ơ�<br>
	 * 4�����������Ϣ����tmpl_value��ֵ���浽Frela_info�ֶ��У�������ʷ�Ķ��ŷ�����Ϣ��¼��ˮ����<br>
	 * 5������ģ���������������ɶ�������<br>
	 * 6�������������·�����
	 * 
	 * @param params
	 * @author Gu.Dongying
	 * @Date 2015��5��4�� ����9:33:53
	 */
	public void sendSM(SendMessageParams params) {
		// 1�����op_code�Ƿ���ȷ
		checkSMSOpCode(params.getOp_code());
		// 2���������������ֻ��Ÿ�ʽ�Ƿ���ȷ��ģ���Ƿ�����
		checkSMSParams(params);
		checkMobilephoneCode(params.getMobile());
		checkTemplate(params.getTmpl_id());
		// 3����֤IP�Ƿ�Ϊ�ڲ���ȨIP���·�֪ͨ�����Ϣֻ���ڲ��������Ļ������Ե��ã������ٽ���Ƶ�����ƿ��ơ�
		checkIPInternalAuthorization(params.getClient_ip());
		// 4�����������Ϣ����tmpl_value��ֵ���浽Frela_info�ֶ��У�������ʷ�Ķ��ŷ�����Ϣ��¼��ˮ����
		MsgSendMessageParams msgParams = MsgSendMessageParams.valueOf(params);
		smsServiceDAO.saveMsgInfo(msgParams);
		// 5������ģ���������������ɶ�������
		MsgTemplateDO template = SMSServiceTemplateUtils.getTemplate(params.getTmpl_id());
		String msg = generateMsg(template.getTmpl_text(), params.getTmpl_value());
		// 6�������������·�����
		sendMsg(params.getMobile(), msg);
	}

	/**
	 * ���op_code�Ƿ���ȷ
	 * 
	 * @param opCode
	 * @author Gu.Dongying
	 * @Date 2015��5��4�� ����9:43:53
	 */
	private void checkSMSOpCode(String opCode) {
		if (!SEND_MSG_OP_CODE.equals(opCode)) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_OPERATION_CODE_PARAMS, "���������!");
		}
	}

	/**
	 * �����������У��
	 * 
	 * @param params
	 * @author Gu.Dongying
	 * @Date 2015��5��4�� ����9:45:04
	 */
	private void checkSMSParams(SendMessageParams params) {
		if (StringUtils.isEmpty(params.getMobile()) || StringUtils.isBlank(params.getMobile())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_MOBILE_PARAMS, "�ֻ����������!");
		}
		if (params.getTmpl_id() == null) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_TEMPLATE_PARAMS, "����ģ��ID����!");
		}
		if (StringUtils.isEmpty(params.getClient_ip()) || StringUtils.isBlank(params.getClient_ip())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_CLIENT_IP_PARAMS, "�û�IP����!");
		}
		if (StringUtils.isEmpty(params.getOp_code()) || StringUtils.isBlank(params.getOp_code())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_OPERATION_CODE_PARAMS, "���������!");
		}
	}

	/**
	 * ����·�������֤���������
	 * 
	 * @param params
	 * @author Gu.Dongying
	 * @Date 2015��4��30�� ����2:26:23
	 */
	private void checkSendInParameters(SendCodeParams params) {
		if (StringUtils.isEmpty(params.getMobile()) || StringUtils.isBlank(params.getMobile())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_MOBILE_PARAMS, "�ֻ��������!");
		}
		if (params.getTmpl_id() == null) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_TEMPLATE_PARAMS, "����ģ��ID����!");
		}
		if (StringUtils.isEmpty(params.getClient_ip()) || StringUtils.isBlank(params.getClient_ip())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_CLIENT_IP_PARAMS, "�û�IP����Ϊ��!");
		}
	}

	/**
	 * �����֤������֤���������
	 * 
	 * @param params
	 * @author Gu.Dongying
	 * @Date 2015��4��30�� ����2:26:23
	 */
	private void checkVerifyInParameters(VerifyCodeParams params) {
		if (StringUtils.isEmpty(params.getMobile()) || StringUtils.isBlank(params.getMobile())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_MOBILE_PARAMS, "�ֻ��������!");
		}
		if (params.getTmpl_id() == null) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_TEMPLATE_PARAMS, "����ģ��ID����!");
		}
		if (StringUtils.isEmpty(params.getClient_ip()) || StringUtils.isBlank(params.getClient_ip())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_CLIENT_IP_PARAMS, "�û�IP����Ϊ��!");
		}
		if (StringUtils.isEmpty(params.getVerify_code()) || StringUtils.isBlank(params.getVerify_code())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_VERIFY_CODE_PARAMS, "��֤�벻��Ϊ��!");
		}
	}

	/**
	 * У���ֻ��Ÿ�ʽ�Ƿ���ȷ
	 * 
	 * @param code
	 *            �ֻ���
	 * @author Gu.Dongying
	 * @Date 2015��4��30�� ����2:31:25
	 */
	private static void checkMobilephoneCode(String code) {
		Pattern pattern = Pattern.compile(SMSServiceCommonConstant.MOBILE_PHONE_REGEXP);
		Matcher matcher = pattern.matcher(code);
		if (!matcher.find()) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_MOBILE_PARAMS, "�ֻ��������!");
		}
	}

	/**
	 * У��ģ���Ƿ�����
	 * 
	 * @param templateId
	 *            ���õ�ģ��ID
	 * @author Gu.Dongying
	 * @Date 2015��4��30�� ����2:33:16
	 */
	private void checkTemplate(BigInteger templateId) {
		if (SMSServiceTemplateUtils.isTemplateExists(templateId.toString())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_TEMPLATE_PARAMS, "����ģ��δ����!");
		}
	}

	/**
	 * 1���ų��Ƿ��ֻ��ź�IP�ڲ���Ƶ�����Ƶİ�������<br>
	 * 2�����ֻ��ź�IP����Ƿ񳬹�Ƶ�����ƣ�������Ƶ����Ϣ
	 * 
	 * @param sParams
	 *            code:�ֻ��� clientIP:�ֻ��Ŷ�Ӧ�Ŀͻ���IP
	 * @author Gu.Dongying
	 * @Date 2015��4��30�� ����3:08:07
	 */
	private void ctrlSendCodeLimit(MsgSendCodeParams sParams) {
		MsgSettingsDO settings = SMSServiceTemplateUtils.readMsgSettings();
		// У���ֻ����Ƿ��ڲ���Ƶ�����Ƶİ�������
		if (settings.getMob_no_whitelists() != null && !settings.getMob_no_whitelists().isEmpty()) {
			final String mobile = sParams.getMobile();
			if (CollectionUtils.exists(settings.getMob_no_whitelists(), new Predicate() {
				public boolean evaluate(Object object) {
					return mobile.equals(object);
				}
			})) {
				return;
			}
		}
		// У��IP�Ƿ��ڲ���Ƶ�����Ƶİ�������
		if (settings.getIp_whitelists() != null && !settings.getIp_whitelists().isEmpty()) {
			final String ip = sParams.getClient_ip();
			if (CollectionUtils.exists(settings.getIp_whitelists(), new Predicate() {
				public boolean evaluate(Object object) {
					return ip.equals(object);
				}
			})) {
				return;
			}
		}

		// ��ѯ�ֻ���Ƶ�����Ƽ�¼
		MobileLimitDO mobileLimit = new MobileLimitDO();
		mobileLimit.setFmobile_no(sParams.getMobile());
		mobileLimit.setFtmpl_id(sParams.getTmpl_id());
		MobileLimitDO currMobileLimit = smsServiceDAO.queryMobileLimit(mobileLimit);
		// �ֻ��ų���Ƶ������
		if (currMobileLimit != null) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_EXCEED_MOBILE_NO_LIMIT, "�ֻ��ų���Ƶ������");
		}

		// ��ѯIP��ַƵ�����Ƽ�¼
		IPLimitDO ipLimit = new IPLimitDO();
		ipLimit.setFclient_ip(sParams.getClient_ip());
		ipLimit.setFtmpl_id(sParams.getTmpl_id());
		IPLimitDO currIPLimit = smsServiceDAO.queryIPLimit(ipLimit);
		// IP��ַ����Ƶ������
		if (currIPLimit != null) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_EXCEED_IP_LIMIT, "IP����Ƶ������");
		}
		// ����Ƶ����Ϣ
		smsServiceDAO.modifySendCodeLimitInfo(sParams);
	}

	/**
	 * ������֤��, ������֤�룬����ʷ����֤����Ϣ��¼��ˮ����
	 * 
	 * @return String ��֤��
	 * @author Gu.Dongying
	 * @Date 2015��4��30�� ����3:11:56
	 */
	private String operationVerifyCode(MsgSendCodeParams params, int len) {
		// ������֤��
		String verifyCode = null;
		if (len > 0) {
			verifyCode = VerifyCodeUtils.generateVerifyCode(len);
		} else {
			verifyCode = VerifyCodeUtils.generateVerifyCode();
		}
		params.setVerify_code(verifyCode);
		// ������֤��,����ʷ����֤����Ϣ��¼��ˮ����
		smsServiceDAO.saveVerifyCodeInfo(params);

		return verifyCode;
	}

	/**
	 * ����ģ���������������ɶ�������
	 * 
	 * @param template
	 *            ����ģ������
	 * @param templateValues
	 *            ����ģ�����
	 * @return String ��������
	 * @author Gu.Dongying
	 * @Date 2015��4��30�� ����3:22:05
	 */
	private String generateMsg(String templateContent, Map<String, String> templateValues) {
		String content = templateContent;
		if (templateValues != null && !templateValues.isEmpty()) {
			Iterator<String> keys = templateValues.keySet().iterator();
			if (keys != null) {
				String key = null;
				while (keys.hasNext()) {
					key = keys.next();
					if (StringUtils.isNotBlank(key) && StringUtils.isNotEmpty(key)) {
						content.replace(SMSServiceCommonConstant.DOLLAR_FLAG + key
								+ SMSServiceCommonConstant.DOLLAR_FLAG, templateValues.get(key));
					}
				}
			}
		}
		return content;
	}

	/**
	 * �����������·����� TODO
	 * 
	 * @author Gu.Dongying
	 * @Date 2015��4��30�� ����3:24:13
	 */
	private void sendMsg(String code, String msg) {
		System.out.println("���ŷ��ͳɹ�----------------");
	}

	/**
	 * ����ҵ��<br>
	 * 1����ѯ��ǰ�ֻ��ź�ģ��ID��Ӧ������������Ϣ<br>
	 * 2��У���Ƿ񳬹���Ч�ڣ�У���Ƿ񳬹���֤����<br>
	 * 3��У���������֤�����·�����֤���Ƿ���ͬ����������֤��Ӧ�Ľ����Ϣ<br>
	 * 
	 * @param params
	 *            MsgVerifyCodeParams
	 * @return String ������֤����͹�����Ϣ
	 * @author Gu.Dongying
	 * @Date 2015��4��30�� ����3:28:27
	 */
	private String checkVerifyCode(MsgVerifyCodeParams params) {
		// ��ѯ��ǰ�ֻ��ź�ģ��ID��Ӧ������������Ϣ
		VerifyCodeInfoDO codeInfo = new VerifyCodeInfoDO();
		codeInfo.setFmobile_no(params.getMobile());
		codeInfo.setFtmpl_id(params.getTmpl_id());
		codeInfo = smsServiceDAO.queryVerifyCodeInfo(codeInfo);
		// У���Ƿ񳬹���Ч�ڣ�
		if (codeInfo.getFexpired_time().getTime() < (new Date()).getTime()) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_VERIFY_CODE_EXPIRED, "��֤���ѹ��ڣ�");
		}
		MsgTemplateDO template = SMSServiceTemplateUtils.getTemplate(params.getTmpl_id());
		// У���Ƿ񳬹���֤����
		if (template.getErr_chk_times() <= codeInfo.getFchk_err_times()
				|| template.getSucc_chk_times() <= codeInfo.getFchk_suc_times()) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_VERIFY_CODE_TIMES, "��֤��У�鳬��У�������");
		}

		// У���������֤�����·�����֤���Ƿ���ͬ����������֤��Ӧ�Ľ����Ϣ
		if (!params.getVerify_code().equals(codeInfo.getFverify_code())) {
			codeInfo.setFchk_err_times(codeInfo.getFchk_err_times() + 1);
		} else {
			codeInfo.setFchk_suc_times(codeInfo.getFchk_suc_times() + 1);
		}
		smsServiceDAO.updateVerifyCodeInfo(codeInfo);
		if (!params.getVerify_code().equals(codeInfo.getFverify_code())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_VERIFY_CODE, "��֤�����");
		}
		// ������֤����͹�����Ϣ
		return codeInfo.getFrela_info();
	}

	/**
	 * ��֤IP�Ƿ�Ϊ�ڲ���ȨIP���·�֪ͨ�����Ϣֻ���ڲ��������Ļ������Ե��ã������ٽ���Ƶ�����ƿ���
	 * 
	 * @param ip
	 *            �ͻ���IP
	 * @author Gu.Dongying
	 * @Date 2015��5��5�� ����10:33:32
	 */
	private void checkIPInternalAuthorization(String ip) {
		MsgSettingsDO msgSettings = SMSServiceTemplateUtils.readMsgSettings();
		final String clientIP = ip;
		if (msgSettings.getIp_whitelists() != null && !msgSettings.getIp_whitelists().isEmpty()) {
			boolean isExists = CollectionUtils.exists(msgSettings.getIp_whitelists(), new Predicate() {
				public boolean evaluate(Object object) {
					return clientIP.equals(object);
				}
			});
			if (!isExists) {
				throw new SMSServiceRetException(SMSServiceRetException.ERR_IP_INTERNAL_AUTHORIZATION,
						"�ͻ���IP�ǲ���ȨIP!");
			}
		} else {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_IP_INTERNAL_AUTHORIZATION,
					"�ͻ���IP�ǲ���ȨIP!");
		}
	}
}