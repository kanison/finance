package com.zcb_app.sms.service.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.app.utils.CommonUtil;
import com.zcb_app.sms.service.facade.dataobject.MsgSettingsDO;
import com.zcb_app.sms.service.facade.dataobject.MsgTemplateDO;
import com.zcb_app.sms.service.facade.dataobject.StrategyDO;

public class SMSServiceTemplateUtils {

	private static final String TEMPLATE_FILE_PATH = "sms_service.template_path";

	private static final String LBL_SEND_NOTIFY_SMS_IPS = "send_notify_sms_ips";

	private static final String LBL_LIMIT_CTRL = "limit_ctrl";

	private static final String LBL_MOB_NO_WHITELIST = "mob_no_whitelist";

	private static final String LBL_IP_WHITELIST = "ip_whitelist";

	private static final String LBL_STRATEGY = "strategy";

	private static final String LBL_TEMPLATES = "templates";

	private static final String LBL_ITEM = "item";

	private static final String LBL_STR_IM_TIMESPAN = "timespan";

	private static final String LBL_STR_IM_MOB_NO_LIMIT = "mob_no_limit";

	private static final String LBL_STR_IM_IP_LIMIT = "ip_limit";

	private static final String LBL_STR_IM_BLOCKTIME = "blocktime";

	private static final String LBL_TEMP_IM_ID = "id";

	private static final String LBL_TEMP_IM_ERR_CHK_TIMES = "err_chk_times";

	private static final String LBL_TEMP_IM_SUCC_CHK_TIMES = "succ_chk_times";

	private static final String LBL_TEMP_IM_EXPIRE_TIME = "expire_time";

	private static final String LBL_TEMP_IM_USE_RELAKEY = "use_relakey";

	private static final String LBL_TEMP_IM_USE_AUTH_CODE_LEN = "auth_code_len";

	private static final String LBL_TEMP_IM_USE_TMPL_TEXT = "tmpl_text";

	private static MsgSettingsDO SETTINGS = null;
	
	/**
	 * 获取指定短信模板
	 * @param templateId
	 * @return MsgTemplateDO
	 * @author Gu.Dongying 
	 * @Date 2015年5月4日 上午11:56:24
	 */
	public static MsgTemplateDO getTemplate(Long templateId){
		if(SETTINGS == null){
			initMsgSettings();
		}
		if(SETTINGS == null || SETTINGS.getTemplates() == null){
			return null;
		}
		return SETTINGS.getTemplates().get(templateId);
	}
	
	/**
	 * 校验指定模板是否存在
	 * @param templateId
	 * @return boolean 是否配置了指定模板
	 * @author Gu.Dongying 
	 * @Date 2015年5月4日 上午11:46:42
	 */
	public static boolean isTemplateExists(String templateId){
		if(SETTINGS == null){
			initMsgSettings();
		}
		if(SETTINGS == null || SETTINGS.getTemplates() == null){
			return false;
		}
		return SETTINGS.getTemplates().containsKey(templateId);
	}
	
	/**
	 * 读取短信模板信息
	 * @return MsgSettingsDO
	 * @author Gu.Dongying 
	 * @Date 2015年5月4日 上午11:42:15
	 */
	public static MsgSettingsDO readMsgSettings(){
		if(SETTINGS == null){
			initMsgSettings();
		}
		return SETTINGS;
	}
	
	/**
	 * 初始化短信模板信息
	 * 
	 * @return MsgSettingsDO templateId
	 * @return MsgSettingsDO
	 * @author Gu.Dongying
	 * @Date 2015年5月4日 上午10:42:51
	 */
	private static void initMsgSettings() {
		InputStream in = null;
		try {
			SAXReader reader = new SAXReader();
			in = new BufferedInputStream(new FileInputStream(CommonUtil.getWebConfig(TEMPLATE_FILE_PATH)));
			if (in != null) {
				Document doc = reader.read(in);
				if (doc != null) {
					SETTINGS = new MsgSettingsDO();
					Element el = doc.getRootElement();
					SETTINGS.setSend_notify_sms_ips(el.elementText(LBL_SEND_NOTIFY_SMS_IPS));
					Element limitCtrl = el.element(LBL_LIMIT_CTRL);
					SETTINGS.setMob_no_whitelist(limitCtrl.elementText(LBL_MOB_NO_WHITELIST));
					SETTINGS.setIp_whitelist(limitCtrl.elementText(LBL_IP_WHITELIST));
					SETTINGS.setStrategys(buildStrategys(limitCtrl.element(LBL_STRATEGY)));
					SETTINGS.setTemplates(buildTemplates(el.element(LBL_TEMPLATES)));
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 构建短信模板信息对象
	 * 
	 * @param templatesEl
	 * @return Map<String,MsgTemplateDO>
	 * @author Gu.Dongying
	 * @Date 2015年5月4日 上午11:06:52
	 */
	@SuppressWarnings("unchecked")
	private static Map<Long, MsgTemplateDO> buildTemplates(Element templatesEl) {
		Map<Long, MsgTemplateDO> templates = null;
		if (templatesEl != null) {
			List<Element> items = templatesEl.elements(LBL_ITEM);
			if (items != null && !items.isEmpty()) {
				MsgTemplateDO template = null;
				templates = new HashMap<Long, MsgTemplateDO>();
				
				String authCodeLen = null;
				String errChkTimes = null;
				String expireTime = null;
				String succChkTimes = null;
				
				for (Element item : items) {
					try {
						template = new MsgTemplateDO();
						
						authCodeLen = item.attributeValue(LBL_TEMP_IM_USE_AUTH_CODE_LEN);
						if(StringUtils.isNotEmpty(authCodeLen) && StringUtils.isNotBlank(authCodeLen)){							
							template.setAuth_code_len(Integer.valueOf(authCodeLen));
						}
						
						errChkTimes = item.attributeValue(LBL_TEMP_IM_ERR_CHK_TIMES);
						if(StringUtils.isNotEmpty(errChkTimes) && StringUtils.isNotBlank(errChkTimes)){							
							template.setErr_chk_times(Integer.valueOf(errChkTimes));
						}
						
						expireTime = item.attributeValue(LBL_TEMP_IM_EXPIRE_TIME);
						if(StringUtils.isNotEmpty(expireTime) && StringUtils.isNotBlank(expireTime)){
							template.setExpire_time(Integer.valueOf(expireTime));
						}
						
						template.setId(item.attributeValue(LBL_TEMP_IM_ID));
						
						succChkTimes = item.attributeValue(LBL_TEMP_IM_SUCC_CHK_TIMES);
						if(StringUtils.isNotEmpty(succChkTimes) && StringUtils.isNotBlank(succChkTimes)){							
							template.setSucc_chk_times(Integer.valueOf(succChkTimes));
						}
						
						template.setTmpl_text(item.attributeValue(LBL_TEMP_IM_USE_TMPL_TEXT));
						
						template.setUse_relakey(SMSServiceCommonConstant.BOOLEAN_VAL_TRUE.equals(item
								.attributeValue(LBL_TEMP_IM_USE_RELAKEY)) ? true : false);
						
						templates.put(new Long(template.getId()), template);
					} catch (NumberFormatException e) {
						e.printStackTrace();
						continue;
					}
				}

			}
		}
		return templates;
	}
	
	/**
	 * 构建短信频率信息对象
	 * 
	 * @param strategyEl
	 * @return List<StrategyDO>
	 * @author Gu.Dongying
	 * @Date 2015年5月4日 上午10:47:17
	 */
	@SuppressWarnings("unchecked")
	private static List<StrategyDO> buildStrategys(Element strategyEl) {
		List<StrategyDO> strategys = null;
		if (strategyEl != null) {
			List<Element> items = strategyEl.elements(LBL_ITEM);
			if (items != null && !items.isEmpty()) {
				StrategyDO strategy = null;
				strategys = new ArrayList<StrategyDO>();
				for (Element item : items) {
					try {
						strategy = new StrategyDO();
						strategy.setBlocktime(new Long(item.attributeValue(LBL_STR_IM_BLOCKTIME)));
						strategy.setIp_limit(Integer.valueOf(item.attributeValue(LBL_STR_IM_IP_LIMIT)));
						strategy.setMob_no_limit(Integer.valueOf(item.attributeValue(LBL_STR_IM_MOB_NO_LIMIT)));
						strategy.setTimespan(new Long(item.attributeValue(LBL_STR_IM_TIMESPAN)));
						strategys.add(strategy);
					} catch (NumberFormatException e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		}
		return strategys;
	} 

}
