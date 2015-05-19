package com.zcb_app.sms.service.code;

import java.util.Date;
import java.util.HashMap;
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
 * 短信服务处理实现类
 * @author Gu.Dongying
 */
public class SMSServiceImpl implements SMSServiceFacade {

	// 发送通知短信操作码
	private static final String SEND_MSG_OP_CODE = "100010003";

	private static final String PLACE_AUTH_CODE = "auth_code";

	/**
	 * 短信服务数据类
	 */
	private SMSServiceDAO smsServiceDAO;

	/**
	 * 获取短信服务数据类
	 * 
	 * @return smsServiceDAO 短信服务数据类
	 */
	public SMSServiceDAO getSmsServiceDAO() {
		return smsServiceDAO;
	}

	/**
	 * 设置短信服务数据类
	 * 
	 * @param smsServiceDAO
	 *            the 短信服务数据类 to set
	 */
	public void setSmsServiceDAO(SMSServiceDAO smsServiceDAO) {
		this.smsServiceDAO = smsServiceDAO;
	}

	/**
	 * 下发短信验证码<br>
	 * 1、检查输入参数，手机号格式是否正确，模板是否配置<br>
	 * 2、按手机号和IP检查是否超过频率限制<br>
	 * 3、生成验证码<br>
	 * 4、保存验证码，将历史的验证码信息记录流水表中<br>
	 * 5、根据模板和输入参数，生成短信内容<br>
	 * 6、调短信网关下发短信<br>
	 * 
	 * @param params
	 *            传入参数
	 * @author Gu.Dongying
	 * @Date 2015年4月30日 下午2:17:12
	 */
	public void sendMCode(SendCodeParams params) {
		// 1、检查输入参数，手机号格式是否正确，模板是否配置
		checkSendInParameters(params);
		checkMobilephoneCode(params.getMobile());
		checkTemplate(params.getTmpl_id(), true);
		MsgSendCodeParams sParams = MsgSendCodeParams.valueOf(params);
		// 2、按手机号和IP检查是否超过频率限制
		smsServiceDAO.ctrlSendCodeLimit(sParams);

		// 3、生成验证码;4、保存验证码，将历史的验证码信息记录流水表中
		MsgTemplateDO template = SMSServiceTemplateUtils.getTemplate(params.getTmpl_id());
		sParams.setExpired_time(template.getExpire_time());
		String verifyCode = operationVerifyCode(sParams, template.getAuth_code_len());

		// 记录下发的短信记录
		SendInfoDO sendInfo = new SendInfoDO();
		sendInfo.setFclient_ip(sParams.getClient_ip());
		sendInfo.setFmobile_no(sParams.getMobile());
		sendInfo.setFtmpl_id(sParams.getTmpl_id());
		smsServiceDAO.addSendInfo(sendInfo);

		Map<String, String> tempValues = params.getTmpl_value();
		if(tempValues == null){
			tempValues = new HashMap<String, String>();
		}
		tempValues.put(PLACE_AUTH_CODE, verifyCode);
		// 5、根据模板和输入参数，生成短信内容
		String msg = generateMsg(template.getTmpl_text(), tempValues);
		// 6、调短信网关下发短信
		sendMsg(params.getMobile(), msg, params.getUse_bak_port());
	}

	/**
	 * 验证短信验证码<br>
	 * 1、检查输入参数，手机号格式是否正确，模板是否配置<br>
	 * 2、查询当前手机号和模板ID对应的最新验码信息<br>
	 * 3、校验是否超过有效期；校验是否超过验证次数<br>
	 * 4、校验输入的验证码与下发的验证码是否相同，并更新验证相应的结果信息<br>
	 * 
	 * @param params
	 *            传入参数
	 * @return String 返回验证结果和关联信息
	 * @author Gu.Dongying
	 * @Date 2015年4月30日 下午2:19:19
	 */
	public String verifyMCode(VerifyCodeParams params) {
		// 检查输入参数，手机号格式是否正确，模板是否配置
		checkVerifyInParameters(params);
		checkMobilephoneCode(params.getMobile());
		checkTemplate(params.getTmpl_id(), true);
		// 校验验证码
		MsgVerifyCodeParams vParams = MsgVerifyCodeParams.valueOf(params);
		return checkVerifyCode(vParams);
	}

	/**
	 * 7.1.3 发送通知短信<br>
	 * 1、检查op_code是否正确<br>
	 * 2、检查输入参数，手机号格式是否正确，模板是否配置<br>
	 * 3、验证IP是否为内部授权IP（下发通知类的消息只有内部白名单的机器可以调用），不再进行频率限制控制。<br>
	 * 4、保存短信消息（将tmpl_value的值保存到Frela_info字段中），将历史的短信发送信息记录流水表中<br>
	 * 5、根据模板和输入参数，生成短信内容<br>
	 * 6、调短信网关下发短信
	 * 
	 * @param params
	 * @author Gu.Dongying
	 * @Date 2015年5月4日 上午9:33:53
	 */
	public void sendSM(SendMessageParams params) {
		// 1、检查op_code是否正确
		checkSMSOpCode(params.getOp_code());
		// 2、检查输入参数，手机号格式是否正确，模板是否配置
		checkSMSParams(params);
		checkMobilephoneCode(params.getMobile());
		checkTemplate(params.getTmpl_id(), false);
		// 3、验证IP是否为内部授权IP（下发通知类的消息只有内部白名单的机器可以调用），不再进行频率限制控制。
		checkIPInternalAuthorization(params.getClient_ip());
		// 4、保存短信消息（将tmpl_value的值保存到Frela_info字段中），将历史的短信发送信息记录流水表中
		MsgSendMessageParams msgParams = MsgSendMessageParams.valueOf(params);
		smsServiceDAO.saveMsgInfo(msgParams);
		// 5、根据模板和输入参数，生成短信内容
		MsgTemplateDO template = SMSServiceTemplateUtils.getTemplate(params.getTmpl_id());
		String msg = generateMsg(template.getTmpl_text(), params.getTmpl_value());
		// 6、调短信网关下发短信
		sendMsg(params.getMobile(), msg, params.getUse_bak_port());
	}

	/**
	 * 检查op_code是否正确
	 * 
	 * @param opCode
	 * @author Gu.Dongying
	 * @Date 2015年5月4日 上午9:43:53
	 */
	private void checkSMSOpCode(String opCode) {
		if (!SEND_MSG_OP_CODE.equals(opCode)) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_OPERATION_CODE_PARAMS, "操作码错误!");
		}
	}

	/**
	 * 输入参数必填校验
	 * 
	 * @param params
	 * @author Gu.Dongying
	 * @Date 2015年5月4日 上午9:45:04
	 */
	private void checkSMSParams(SendMessageParams params) {
		if (StringUtils.isEmpty(params.getMobile()) || StringUtils.isBlank(params.getMobile())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_MOBILE_PARAMS, "手机号输入错误!");
		}
		if (params.getTmpl_id() == null) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_TEMPLATE_PARAMS, "短信模板ID错误!");
		}
		if (StringUtils.isEmpty(params.getClient_ip()) || StringUtils.isBlank(params.getClient_ip())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_CLIENT_IP_PARAMS, "用户IP错误!");
		}
		if (StringUtils.isEmpty(params.getOp_code()) || StringUtils.isBlank(params.getOp_code())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_OPERATION_CODE_PARAMS, "操作码错误!");
		}
	}

	/**
	 * 检查下发短信验证码输入参数
	 * 
	 * @param params
	 * @author Gu.Dongying
	 * @Date 2015年4月30日 下午2:26:23
	 */
	private void checkSendInParameters(SendCodeParams params) {
		if (StringUtils.isEmpty(params.getMobile()) || StringUtils.isBlank(params.getMobile())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_MOBILE_PARAMS, "手机号码错误!");
		}
		if (params.getTmpl_id() == null) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_TEMPLATE_PARAMS, "短信模板ID错误!");
		}
		if (StringUtils.isEmpty(params.getClient_ip()) || StringUtils.isBlank(params.getClient_ip())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_CLIENT_IP_PARAMS, "用户IP不能为空!");
		}
	}

	/**
	 * 检查验证短信验证码输入参数
	 * 
	 * @param params
	 * @author Gu.Dongying
	 * @Date 2015年4月30日 下午2:26:23
	 */
	private void checkVerifyInParameters(VerifyCodeParams params) {
		if (StringUtils.isEmpty(params.getMobile()) || StringUtils.isBlank(params.getMobile())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_MOBILE_PARAMS, "手机号码错误!");
		}
		if (params.getTmpl_id() == null) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_TEMPLATE_PARAMS, "短信模板ID错误!");
		}
		if (StringUtils.isEmpty(params.getClient_ip()) || StringUtils.isBlank(params.getClient_ip())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_CLIENT_IP_PARAMS, "用户IP不能为空!");
		}
		if (StringUtils.isEmpty(params.getVerify_code()) || StringUtils.isBlank(params.getVerify_code())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_VERIFY_CODE_PARAMS, "验证码不能为空!");
		}
	}

	/**
	 * 校验手机号格式是否正确
	 * 
	 * @param code
	 *            手机号
	 * @author Gu.Dongying
	 * @Date 2015年4月30日 下午2:31:25
	 */
	private static void checkMobilephoneCode(String code) {
		Pattern pattern = Pattern.compile(SMSServiceCommonConstant.MOBILE_PHONE_REGEXP);
		Matcher matcher = pattern.matcher(code);
		if (!matcher.find()) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_MOBILE_PARAMS, "手机号码错误!");
		}
	}

	/**
	 * 校验模板是否配置
	 * 
	 * @param templateId
	 *            配置的模板ID
	 * @author Gu.Dongying
	 * @Date 2015年4月30日 下午2:33:16
	 */
	private void checkTemplate(Long templateId, boolean checkAttrs) {
		if (!SMSServiceTemplateUtils.isTemplateExists(templateId)) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_TEMPLATE_NOT_EXISTS, "短信模板不存在!");
		}
		
		MsgTemplateDO template = SMSServiceTemplateUtils.getTemplate(templateId);
		if(StringUtils.isBlank(template.getTmpl_text()) && StringUtils.isEmpty(template.getTmpl_text())){
			throw new SMSServiceRetException(SMSServiceRetException.ERR_TEMPLATE_PARAMS, "短信模板配置错误!");
		}
		if(checkAttrs && (template.getAuth_code_len() < 1 
				|| template.getErr_chk_times() < 1 
				|| template.getSucc_chk_times() < 1 
				|| template.getExpire_time() < 1)){
			throw new SMSServiceRetException(SMSServiceRetException.ERR_TEMPLATE_PARAMS, "短信模板配置错误!");
		}
	}

	/**
	 * 生成验证码, 保存验证码，将历史的验证码信息记录流水表中
	 * 
	 * @return String 验证码
	 * @author Gu.Dongying
	 * @Date 2015年4月30日 下午3:11:56
	 */
	private String operationVerifyCode(MsgSendCodeParams params, int len) {
		// 生成验证码
		String verifyCode = null;
		if (len > 0) {
			verifyCode = VerifyCodeUtils.generateVerifyCode(len);
		} else {
			verifyCode = VerifyCodeUtils.generateVerifyCode();
		}
		params.setVerify_code(verifyCode);
		// 保存验证码,将历史的验证码信息记录流水表中
		smsServiceDAO.saveVerifyCodeInfo(params);

		return verifyCode;
	}

	/**
	 * 根据模板和输入参数，生成短信内容
	 * 
	 * @param template
	 *            短信模板内容
	 * @param templateValues
	 *            短信模板参数
	 * @return String 短信内容
	 * @author Gu.Dongying
	 * @Date 2015年4月30日 下午3:22:05
	 */
	private String generateMsg(String templateContent, Map<String, String> templateValues) {
		if (templateValues != null && !templateValues.isEmpty()) {
			Iterator<String> keys = templateValues.keySet().iterator();
			if (keys != null) {
				String key = null;
				while (keys.hasNext()) {
					key = keys.next();
					if (StringUtils.isNotBlank(key) && StringUtils.isNotEmpty(key)) {
						templateContent = templateContent.replace(SMSServiceCommonConstant.DOLLAR_FLAG + key
								+ SMSServiceCommonConstant.DOLLAR_FLAG, templateValues.get(key));
					}
				}
			}
		}
		return templateContent;
	}

	/**
	 * 调短信网关下发短信 TODO
	 * @param mobileCode 手机号
	 * @param msg 短信内容
	 * @author Gu.Dongying 
	 * @Date 2015年5月12日 上午11:59:14
	 */
	private void sendMsg(String mobileCode, String msg, int useBakPort) {
		if(SMSServiceCommonConstant.USE_BAK_PORT == useBakPort){
			System.out.println("使用备份渠道发送短信成功 \n 手机号：" + mobileCode + " \n短信内容：" + msg);
		}else{			
			System.out.println("短信发送成功 \n 手机号：" + mobileCode + " \n短信内容：" + msg);
		}
	}

	/**
	 * 处理业务：<br>
	 * 1、查询当前手机号和模板ID对应的最新验码信息<br>
	 * 2、校验是否超过有效期；校验是否超过验证次数<br>
	 * 3、校验输入的验证码与下发的验证码是否相同，并更新验证相应的结果信息<br>
	 * 
	 * @param params
	 *            MsgVerifyCodeParams
	 * @return String 返回验证结果和关联信息
	 * @author Gu.Dongying
	 * @Date 2015年4月30日 下午3:28:27
	 */
	private String checkVerifyCode(MsgVerifyCodeParams params) {
		// 查询当前手机号和模板ID对应的最新验码信息
		VerifyCodeInfoDO codeInfo = new VerifyCodeInfoDO();
		codeInfo.setFmobile_no(params.getMobile());
		codeInfo.setFtmpl_id(params.getTmpl_id());
		codeInfo = smsServiceDAO.queryMsgInfo(codeInfo);
		if(codeInfo == null){
			throw new SMSServiceRetException(SMSServiceRetException.ERR_VERIFYCODE_NOT_EXISTS, "短信验证码已丢失!");
		}
		// 校验是否超过有效期；
		if (codeInfo.getFexpired_time().getTime() < (new Date()).getTime()) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_VERIFY_CODE_EXPIRED, "验证码已过期！");
		}
		MsgTemplateDO template = SMSServiceTemplateUtils.getTemplate(params.getTmpl_id());
		// 校验是否超过验证次数
		if (template.getErr_chk_times() <= codeInfo.getFchk_err_times()
				|| template.getSucc_chk_times() <= codeInfo.getFchk_suc_times()) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_VERIFY_CODE_TIMES, "验证码校验超过校验次数！");
		}

		//使用关联Key进行校验
		if(template.isUse_relakey()){
			if(StringUtils.isBlank(params.getRelation_key()) 
					|| StringUtils.isEmpty(params.getRelation_key())){
				throw new SMSServiceRetException(SMSServiceRetException.ERR_RELATION_KEY_NONE, "未传关联Key！");
			}
			if(!params.getRelation_key().equals(codeInfo.getFrela_key())){
				throw new SMSServiceRetException(SMSServiceRetException.ERR_RELATION_KEY, "关联Key错误！");
			}
		}
		
		// 校验输入的验证码与下发的验证码是否相同，并更新验证相应的结果信息
		if (!params.getVerify_code().equals(codeInfo.getFverify_code())) {
			codeInfo.setFchk_err_times(codeInfo.getFchk_err_times() + 1);
		} else {
			codeInfo.setFchk_suc_times(codeInfo.getFchk_suc_times() + 1);
		}
		smsServiceDAO.updateVerifyCodeInfo(codeInfo);
		if (!params.getVerify_code().equals(codeInfo.getFverify_code())) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_VERIFY_CODE, "验证码错误！");
		}
		// 返回验证结果和关联信息
		return codeInfo.getFrela_info();
	}

	/**
	 * 验证IP是否为内部授权IP（下发通知类的消息只有内部白名单的机器可以调用），不再进行频率限制控制
	 * 
	 * @param ip
	 *            客户端IP
	 * @author Gu.Dongying
	 * @Date 2015年5月5日 上午10:33:32
	 */
	private void checkIPInternalAuthorization(String ip) {
		MsgSettingsDO msgSettings = SMSServiceTemplateUtils.readMsgSettings();
		final String clientIP = ip;
		if (msgSettings.getSend_notify_sms_ipslist() != null && !msgSettings.getSend_notify_sms_ipslist().isEmpty()) {
			boolean isExists = CollectionUtils.exists(msgSettings.getSend_notify_sms_ipslist(), new Predicate() {
				public boolean evaluate(Object object) {
					return clientIP.equals(object);
				}
			});
			if (!isExists) {
				throw new SMSServiceRetException(SMSServiceRetException.ERR_IP_INTERNAL_AUTHORIZATION,
						"客户端IP非内部授权IP!");
			}
		} else {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_IP_INTERNAL_AUTHORIZATION,
					"客户端IP非内部授权IP!");
		}
	}
}
