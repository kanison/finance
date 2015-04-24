/**
 * 
 */
package com.zhaocb.common.signature.imp;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.map.LRUMap;
import org.springframework.beans.BeanUtils;

import com.tenpay.sm.lang.config.ReloadableAppConfig;
import com.zhaocb.common.signature.dao.MerchantAccessProfileDAO;
import com.zhaocb.common.signature.facade.SignResult;
import com.zhaocb.common.signature.po.DefaultSettingPO;

/**
 * @author eniacli 商户默认参数管理
 */
public class SignatureDefaultSettingManager {
	private Map<String, SettingContainer> defaultSettings;
	private long expireDuration = 1800000;

	private MerchantAccessProfileDAO merchantAccessProfileDAO;

	@SuppressWarnings("unchecked")
	public SignatureDefaultSettingManager() {
		int signatureCacheSize = 100000;
		String signatureCacheSizeStr = ReloadableAppConfig.appConfig
				.get("signature.cacheSize");
		if (signatureCacheSizeStr != null) {
			signatureCacheSize = Integer.valueOf(signatureCacheSizeStr.trim());
		}
		defaultSettings = java.util.Collections.synchronizedMap(new LRUMap(
				signatureCacheSize));

		// 分钟转毫秒
		String expireDurationStr = ReloadableAppConfig.appConfig
				.get("signature.expireDuration");
		if (expireDurationStr != null) {
			expireDuration = expireDuration
					+ Integer.valueOf(expireDurationStr.trim()) * 60 * 1000;
		}

	}

	public SignResult getDefaultSetting(String chnid) {

		SettingContainer defaultSetting = defaultSettings.get(chnid);
		if (defaultSetting == null || !defaultSetting.isValid()) {
			if (defaultSetting == null)
				defaultSetting = new SettingContainer();

			try {
				DefaultSettingPO defaultSettingPO = null;
				if (merchantAccessProfileDAO != null)
					defaultSettingPO = merchantAccessProfileDAO
							.selectDefaultSettingByerchantID(chnid);

				if (defaultSettingPO == null) {
					return defaultSetting.getSetting();
				}
				SignResult signSetting = new SignResult();
				BeanUtils.copyProperties(defaultSettingPO, signSetting);
				defaultSetting.setSetting(signSetting);
			} catch (Exception e) {
				return defaultSetting.getSetting();
			}
			defaultSettings.put(chnid, defaultSetting);
			Date currentDate = new Date();
			currentDate.setTime(currentDate.getTime() + expireDuration);
			defaultSetting.setExpireTime(currentDate);
		}
		return defaultSetting.getSetting();
	}

	public MerchantAccessProfileDAO getMerchantAccessProfileDAO() {
		return merchantAccessProfileDAO;
	}

	public void setMerchantAccessProfileDAO(
			MerchantAccessProfileDAO merchantAccessProfileDAO) {
		this.merchantAccessProfileDAO = merchantAccessProfileDAO;
	}

}
