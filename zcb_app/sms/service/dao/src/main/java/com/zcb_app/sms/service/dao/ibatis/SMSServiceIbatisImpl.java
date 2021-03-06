package com.zcb_app.sms.service.dao.ibatis;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.ibatis.sqlmap.client.SqlMapClient;
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
	 * 保存下发的验证码
	 * @param verifyCode
	 * @author Gu.Dongying 
	 * @Date 2015年5月4日 下午2:52:30
	 */
	private void createMsgInfo(VerifyCodeInfoDO verifyCode){
		if(verifyCode == null){
			throw new SMSServiceRetException(SMSServiceRetException.ERR_SEND_CODE_PARAMS, "下发短信验证码参数错误!");
		}
		verifyCode.genDataTableIndex();
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertMsgInfo", verifyCode);
	}

	/** 
	 * 记录下发验证码的流水日志
	 * @author Gu.Dongying 
	 * @Date 2015年5月4日 下午4:29:35
	 */
	private void createMsgLog(MsgLogDO msgLog) {
		if(msgLog == null){
			throw new SMSServiceRetException(SMSServiceRetException.ERR_SEND_CODE_PARAMS, "记录短信流水错误!");
		}
		msgLog.genDataTableIndex();
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertMsgLog", msgLog);
	}

	/** 
	 * 处理业务<br>
	 * 1、保存下发的验证码<br>
	 * 2、将历史的验证码信息记录流水表中<br>
	 * @see com.zcb_app.mvcode.service.dao.SMSServiceDAO#saveVerifyCodeInfo(com.zcb_app.mvcode.service.facade.dataobject.VerifyCodeInfoDO)
	 * @author Gu.Dongying 
	 * @Date 2015年5月4日 下午4:33:32
	 */
	@Transactional
	public void saveVerifyCodeInfo(MsgSendCodeParams params) {
		
		VerifyCodeInfoDO info = new VerifyCodeInfoDO();
		//查询出原有验证码信息
		info.setFmobile_no(params.getMobile());
		info.setFtmpl_id(params.getTmpl_id());
		VerifyCodeInfoDO historyInfo = queryMsgInfo(info);
		//保存下发的验证码
		info = new VerifyCodeInfoDO();
		info.setFcheck_time(params.getCheck_time());
		info.setFchk_err_times(params.getChk_err_times());
		info.setFchk_suc_times(params.getChk_suc_times());
		info.setFclient_ip(params.getClient_ip());
		info.setI_expired_time(params.getExpired_time());
		info.setFmobile_no(params.getMobile());
		info.setFrela_info(params.getRelation_info());
		info.setFrela_key(params.getRelation_key());
		info.setFsend_time(params.getSend_time());
		info.setFtmpl_id(params.getTmpl_id());
		info.setFverify_code(params.getVerify_code());
		createMsgInfo(info);
		if(historyInfo != null){			
			//将历史的验证码信息记录流水表中 
			MsgLogDO msgLog = new MsgLogDO();
			msgLog.setFcheck_time(historyInfo.getFcheck_time());
			msgLog.setFchk_err_times(historyInfo.getFchk_err_times());
			msgLog.setFchk_suc_times(historyInfo.getFchk_suc_times());
			msgLog.setFclient_ip(historyInfo.getFclient_ip());
			msgLog.setFexpired_time(historyInfo.getFexpired_time());
			msgLog.setFmobile_no(historyInfo.getFmobile_no());
			msgLog.setFrela_info(historyInfo.getFrela_info());
			msgLog.setFrela_key(historyInfo.getFrela_key());
			msgLog.setFsend_time(historyInfo.getFsend_time());
			msgLog.setFtmpl_id(historyInfo.getFtmpl_id());
			msgLog.setFverify_code(historyInfo.getFverify_code());
			createMsgLog(msgLog);
		}
	}
	
	/**
	 * 查询手机号频率受限记录
	 * @param mobileLimit 手机号、模板ID
	 * @return MobileLimitDO
	 * @author Gu.Dongying 
	 * @throws SQLException 
	 * @Date 2015年5月5日 上午9:43:14
	 */
	private MobileLimitDO queryMobileLimit(SqlMapClient client, MobileLimitDO mobileLimit) throws SQLException{
		//SqlMapClientTemplate client = getSqlMapClientTemplate();
		MobileLimitDO mobile = (MobileLimitDO)client.queryForObject("queryMobileLimit", mobileLimit);
		return mobile;
	}
	
	/**
	 * 查询IP频率受限记录
	 * @param ipLimit IP、模板ID
	 * @return IPLimitDO
	 * @author Gu.Dongying 
	 * @throws SQLException 
	 * @Date 2015年5月5日 上午9:43:14
	 */
	private IPLimitDO queryIPLimit(SqlMapClient client, IPLimitDO ipLimit) throws SQLException{
		//SqlMapClientTemplate client = getSqlMapClientTemplate();
		IPLimitDO ip = (IPLimitDO)client.queryForObject("queryIPLimit", ipLimit);
		return ip;
	}

	/** 
	 * 保存短信消息（将tmpl_value的值保存到Frela_info字段中），将历史的短信发送信息记录流水表中
	 * @see com.zcb_app.mvcode.service.dao.SMSServiceDAO#saveMsgInfo(com.zcb_app.mvcode.service.dao.type.MsgSendMessageParams)
	 * @author Gu.Dongying 
	 * @Date 2015年5月5日 上午11:02:01
	 */
	@Transactional
	public void saveMsgInfo(MsgSendMessageParams params) {
		//保存短信消息
		VerifyCodeInfoDO info = new VerifyCodeInfoDO();
		//查询出原有短信信息
		info.setFmobile_no(params.getMobile_no());
		info.setFtmpl_id(params.getTmpl_id());
		VerifyCodeInfoDO historyInfo = queryMsgInfo(info);
		//保存下发的验证码
		info = new VerifyCodeInfoDO();
		info.setFclient_ip(params.getClient_ip());
		info.setFmobile_no(params.getMobile_no());
		info.setFrela_info(params.getTmpl_value_str());
		info.setFtmpl_id(params.getTmpl_id());
		createMsgInfo(info);
		
		//将历史的短信发送信息记录流水表中
		if(historyInfo != null){			
			MsgLogDO msgLog = new MsgLogDO();
			msgLog.setFclient_ip(historyInfo.getFclient_ip());
			msgLog.setFmobile_no(historyInfo.getFmobile_no());
			msgLog.setFrela_info(historyInfo.getFrela_info());
			msgLog.setFtmpl_id(historyInfo.getFtmpl_id());
			createMsgLog(msgLog);
		}
	}
	
	/**
	 * 查询当前手机号和模板ID对应的最新验码信息
	 * @param codeInfo 当前手机号和模板ID
	 * @return VerifyCodeInfoDO
	 * @author Gu.Dongying 
	 * @Date 2015年5月5日 上午11:45:52
	 */
	public VerifyCodeInfoDO queryMsgInfo(VerifyCodeInfoDO codeInfo){
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		codeInfo.genDataTableIndex();
		VerifyCodeInfoDO vCodeInfo = (VerifyCodeInfoDO)client.queryForObject("queryMsgInfo", codeInfo);
		return vCodeInfo;
	}
	
	/**
	 * 更新验证相应的结果信息
	 * @param codeInfo 当前手机号和模板ID
	 * @author Gu.Dongying 
	 * @Date 2015年5月5日 下午12:57:41
	 */
	@Transactional
	public void updateVerifyCodeInfo(VerifyCodeInfoDO codeInfo){
		//锁住更新数据
		codeInfo.setQuerylock(true);
		queryMsgInfo(codeInfo);
		
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		int effected_rows = client.update("updateVerifyCodeInfo", codeInfo);
		if (1 != effected_rows) {
			throw new SMSServiceRetException(SMSServiceRetException.ERR_EFFECTED_ROWS, "更新的影响行数不为1");
		}
	}

	/** 
	 * 新增发送短信信息记录
	 * @see com.zcb_app.mvcode.service.dao.SMSServiceDAO#addSendInfo(com.zcb_app.mvcode.service.facade.dataobject.SendInfoDO)
	 * @author Gu.Dongying 
	 * @Date 2015年5月5日 下午5:41:18
	 */
	@Transactional
	public void addSendInfo(SendInfoDO sendInfo) {
		sendInfo.genDataTableIndex();
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertSendInfo", sendInfo);
	}
	
	/**
	 * 添加手机号频率限制记录
	 * @param mobileLimit 手机号频率限制信息
	 * @author Gu.Dongying 
	 * @Date 2015年5月6日 上午9:25:01
	 */
	@Transactional
	public void addMobileLimitInfo(MobileLimitDO mobileLimit){
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertMobileLimit", mobileLimit);
	}
	
	/**
	 * 添加IP地址频率限制信息记录
	 * @param ipLimit IP地址频率限制信息
	 * @author Gu.Dongying 
	 * @Date 2015年5月6日 上午9:26:08
	 */
	@Transactional
	public void addIPLimitInfo(IPLimitDO ipLimit){
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertIPLimit", ipLimit);
	}

	/** 
	 * 更新发送验证码频率限制的信息
	 * @author Gu.Dongying 
	 * @throws SQLException 
	 * @Date 2015年5月6日 上午10:37:25
	 */
	private void modifySendCodeLimitInfo(SqlMapClient client,MsgSendCodeParams scParams) throws SQLException {
		MsgSettingsDO settings = SMSServiceTemplateUtils.readMsgSettings();
		List<StrategyDO> strategys = settings.getStrategys();
		if (strategys != null && !strategys.isEmpty()) {
			// 手机号频率
			SendInfoDO mobileSendInfo = new SendInfoDO();
			mobileSendInfo.setFmobile_no(scParams.getMobile());
			mobileSendInfo.setFtmpl_id(scParams.getTmpl_id());
			// IP频率
			SendInfoDO ipSendInfo = new SendInfoDO();
			ipSendInfo.setFclient_ip(scParams.getClient_ip());
			ipSendInfo.setFmobile_no(scParams.getMobile());
			ipSendInfo.setFtmpl_id(scParams.getTmpl_id());

			MobileLimitDO mobileLimit;
			IPLimitDO ipLimit;
			for (StrategyDO strategy : strategys) {
				if (strategy != null) {
					mobileSendInfo.setTimespan(strategy.getTimespan());
					// 达到手机号频率受限峰值，添加受限记录
					if (querySendCodeInfoMoblieCount(client, mobileSendInfo) >= strategy.getMob_no_limit() - 1) {
						try {
							mobileLimit = new MobileLimitDO();
							mobileLimit.setFblock_timespan(strategy.getBlocktime());
							mobileLimit.setFmobile_no(scParams.getMobile());
							mobileLimit.setFtmpl_id(scParams.getTmpl_id());
							addMobileLimitInfo(mobileLimit);
						} catch (Exception e) {
							LOG.error("添加手机号受限频率信息失败（Mobile：" + scParams.getMobile() + "；TemplateID:"
									+ scParams.getTmpl_id() + "）");
						}
					}

					ipSendInfo.setTimespan(strategy.getTimespan());
					// 达到IP频率受限峰值，添加受限记录
					if (querySendCodeInfoIPCount(client, ipSendInfo) >= strategy.getIp_limit() - 1) {
						try {
							ipLimit = new IPLimitDO();
							ipLimit.setFblock_timespan(strategy.getBlocktime());
							ipLimit.setFclient_ip(scParams.getClient_ip());
							ipLimit.setFtmpl_id(scParams.getTmpl_id());
							addIPLimitInfo(ipLimit);
						} catch (Exception e) {
							LOG.error("添加IP受限频率信息失败（IP：" + scParams.getMobile() + "；TemplateID:"
									+ scParams.getTmpl_id() + "）");
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * 1、排除是否手机号和IP在不受频率限制的白名单内<br>
	 * 2、按手机号和IP检查是否超过频率限制，并更新频率信息
	 * 
	 * @param sParams
	 *            code:手机号 clientIP:手机号对应的客户端IP
	 * @author Gu.Dongying
	 * @Date 2015年4月30日 下午3:08:07
	 */
	//@Transactional
	public synchronized void ctrlSendCodeLimit(MsgSendCodeParams sParams) {
		SqlMapClient sqlMapClient = this.getSqlMapClient();
		try {
			sqlMapClient.startTransaction();
			
			MsgSettingsDO settings = SMSServiceTemplateUtils.readMsgSettings();
			// 校验手机号是否在不受频率限制的白名单内
			if (settings.getMob_no_whitelists() != null && !settings.getMob_no_whitelists().isEmpty()) {
				final String mobile = sParams.getMobile();
				if (!CollectionUtils.exists(settings.getMob_no_whitelists(), new Predicate() {
					public boolean evaluate(Object object) {
						return mobile.equals(object);
					}
				})) {
					// 查询手机号频率限制记录
					MobileLimitDO mobileLimit = new MobileLimitDO();
					mobileLimit.setFmobile_no(sParams.getMobile());
					mobileLimit.setFtmpl_id(sParams.getTmpl_id());
					MobileLimitDO currMobileLimit = queryMobileLimit(sqlMapClient, mobileLimit);;
					// 手机号超过频率限制
					if (currMobileLimit != null) {
						throw new SMSServiceRetException(SMSServiceRetException.ERR_EXCEED_MOBILE_NO_LIMIT, "手机号超过频率限制");
					}
				}
			}
			// 校验IP是否在不受频率限制的白名单内
			if (settings.getIp_whitelists() != null && !settings.getIp_whitelists().isEmpty()) {
				final String ip = sParams.getClient_ip();
				if (!CollectionUtils.exists(settings.getIp_whitelists(), new Predicate() {
					public boolean evaluate(Object object) {
						return ip.equals(object);
					}
				})) {
					// 查询IP地址频率限制记录
					IPLimitDO ipLimit = new IPLimitDO();
					ipLimit.setFclient_ip(sParams.getClient_ip());
					ipLimit.setFtmpl_id(sParams.getTmpl_id());
					IPLimitDO currIPLimit = queryIPLimit(sqlMapClient, ipLimit);
					// IP地址超过频率限制
					if (currIPLimit != null) {
						throw new SMSServiceRetException(SMSServiceRetException.ERR_EXCEED_IP_LIMIT, "IP超过频率限制");
					}
				}
			}
			
			// 更新频率信息
			modifySendCodeLimitInfo(sqlMapClient, sParams);
			
			// 记录下发的短信记录
			SendInfoDO sendInfo = new SendInfoDO();
			sendInfo.setFclient_ip(sParams.getClient_ip());
			sendInfo.setFmobile_no(sParams.getMobile());
			sendInfo.setFtmpl_id(sParams.getTmpl_id());
			sendInfo.genDataTableIndex();
			sqlMapClient.insert("insertSendInfo", sendInfo);
			
			sqlMapClient.commitTransaction();
		} catch (SQLException e) {
			throw new SMSServiceRetException(SMSServiceRetException.SYSTEM_ERROR, "频率限制系统错误");
		}finally{
			try {
				sqlMapClient.endTransaction();
			} catch (SQLException e) {
				throw new SMSServiceRetException(SMSServiceRetException.SYSTEM_ERROR, "频率限制系统错误");
			}
		}
	}
	
	private int querySendCodeInfoMoblieCount(SqlMapClient client, SendInfoDO sendInfo) throws SQLException {
		// SqlMapClientTemplate client = getSqlMapClientTemplate();
		sendInfo.genDataTableIndex();
		int count = (Integer) client.queryForObject("querySendCodeInfoMoblieCount", sendInfo);
		return count;
	}

	private int querySendCodeInfoIPCount(SqlMapClient client, SendInfoDO sendInfo) throws SQLException {
		// SqlMapClientTemplate client = getSqlMapClientTemplate();
		sendInfo.genDataTableIndex();
		int count = (Integer) client.queryForObject("querySendCodeInfoIPCount", sendInfo);
		return count;
	}
	
}
