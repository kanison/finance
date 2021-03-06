package com.zcb_app.sms.service.facade.dataobject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 模板配置信息
 * @author Gu.Dongying
 */
public class MsgSettingsDO {

	private static final String SEMICOLON = ";";
	
	private List<StrategyDO> strategys;
	
	private String mob_no_whitelist;//	不受频率限制的手机号白名单，以分号分隔
	
	private List<String> mob_no_whitelists;
	
	private String ip_whitelist;//	不受频率限制的IP白名单，以分号分隔
	
	private List<String> ip_whitelists;
	
	private String send_notify_sms_ips;//	内部下发通知消息的IP白名单，以分号分隔
	
	private List<String> send_notify_sms_ipslist;
	
	private Map<Long, MsgTemplateDO> templates;

	public List<StrategyDO> getStrategys() {
		return strategys;
	}

	public void setStrategys(List<StrategyDO> strategys) {
		this.strategys = strategys;
	}

	public String getMob_no_whitelist() {
		return mob_no_whitelist;
	}

	public void setMob_no_whitelist(String mob_no_whitelist) {
		this.mob_no_whitelist = mob_no_whitelist;
		if(StringUtils.isNotBlank(this.mob_no_whitelist) 
				&& StringUtils.isNotEmpty(this.mob_no_whitelist)){
			this.setMob_no_whitelists(Arrays.asList(this.mob_no_whitelist.split(SEMICOLON)));
		}
	}

	private void setMob_no_whitelists(List<String> mob_no_whitelists) {
		this.mob_no_whitelists = mob_no_whitelists;
	}

	private void setIp_whitelists(List<String> ip_whitelists) {
		this.ip_whitelists = ip_whitelists;
	}

	public List<String> getMob_no_whitelists() {
		return mob_no_whitelists;
	}

	public String getIp_whitelist() {
		return ip_whitelist;
	}

	public void setIp_whitelist(String ip_whitelist) {
		this.ip_whitelist = ip_whitelist;
		if(StringUtils.isNotBlank(this.ip_whitelist) 
				&& StringUtils.isNotEmpty(this.ip_whitelist)){
			this.setIp_whitelists(Arrays.asList(this.ip_whitelist.split(SEMICOLON)));
		}
	}

	public List<String> getIp_whitelists() {
		return ip_whitelists;
	}

	public String getSend_notify_sms_ips() {
		return send_notify_sms_ips;
	}

	public void setSend_notify_sms_ips(String send_notify_sms_ips) {
		this.send_notify_sms_ips = send_notify_sms_ips;
		if(StringUtils.isNotBlank(this.send_notify_sms_ips) 
				&& StringUtils.isNotEmpty(this.send_notify_sms_ips)){
			this.setSend_notify_sms_ipslist(Arrays.asList(this.send_notify_sms_ips.split(SEMICOLON)));
		}
	}

	/**
	 * 获取send_notify_sms_ipslist
	 * @return send_notify_sms_ipslist send_notify_sms_ipslist
	 */
	public List<String> getSend_notify_sms_ipslist() {
		return send_notify_sms_ipslist;
	}

	/**
	 * 设置send_notify_sms_ipslist
	 * @param send_notify_sms_ipslist the send_notify_sms_ipslist to set
	 */
	public void setSend_notify_sms_ipslist(List<String> send_notify_sms_ipslist) {
		this.send_notify_sms_ipslist = send_notify_sms_ipslist;
	}

	public Map<Long, MsgTemplateDO> getTemplates() {
		return templates;
	}

	public void setTemplates(Map<Long, MsgTemplateDO> templates) {
		this.templates = templates;
	}
	
}
