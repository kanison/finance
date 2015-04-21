package com.zhaocb.common.signature.service;

import java.security.Key;

import org.apache.log4j.Logger;

import com.app.aop.annotation.LogMethod;
import com.zhaocb.common.signature.facade.SignParams;
import com.zhaocb.common.signature.facade.SignResult;
import com.zhaocb.common.signature.facade.SignatureServiceFacade;
import com.zhaocb.common.signature.facade.VerifySignatureParams;
import com.zhaocb.common.signature.imp.SignKeyManager;
import com.zhaocb.common.signature.imp.SignatureDefaultSettingManager;
import com.zhaocb.common.signature.imp.SignatureManager;
import com.zhaocb.common.signature.imp.SignatureResult;



public class SignatureServiceImpl implements SignatureServiceFacade {
	private SignKeyManager signKeyManager;
	private SignatureManager signatureManager;
	private SignatureDefaultSettingManager signatureDefaultSettingManager;
	private static final Logger LOG = Logger
			.getLogger(SignatureServiceImpl.class);

	/**
	 * 签名
	 */
	@LogMethod
	public SignResult sign(SignParams signParams) {
		if (signParams.getChnid() == null) {
			LOG.warn("Chnid is null");
			return null;
		}

		boolean paramInvalid = false;
		if (signParams.getSignType() == null) {
			signParams.setSignType("MD5");
			paramInvalid = true;
		}
		if (signParams.getCharset() == null) {
			signParams.setCharset("GBK");
			paramInvalid = true;
		}
		if (paramInvalid) { // 查询商户默认配置
			SignResult signResult = getDefaultParams(signParams.getChnid());
			if (signResult == null) {
				signResult = new SignResult();
				signResult.setCharset("GBK");
				signResult.setSignType("MD5");
				signResult.setSignKeyIndex(1);

			}
			return signResult;// 接收端要根据signResult.getSign()!=null判断结果
			/*
			 * if (signResult.getCharset() != null)
			 * signParams.setCharset(signResult.getCharset());
			 * signParams.setKeyIndex(signResult.getSignKeyIndex()); if
			 * (signResult.getSignType() != null)
			 * signParams.setSignType(signResult.getSignType()); return null;
			 */
		}

		try {
			Key privateKey = signKeyManager.getPrivateKey(
					signParams.getChnid(), signParams.getSignType(), signParams
							.getKeyIndex());
			if (privateKey == null){
				LOG.warn("privateKey null");
				return null;
			}
			SignResult ret = new SignResult();
			ret.setSignType(signParams.getSignType());
			ret.setCharset(signParams.getCharset());
			ret.setSignKeyIndex(signParams.getKeyIndex());
			ret.setPartner(signParams.getChnid());
			ret.setSign(signatureManager.sign(signParams.getSignType(),
					signParams.getContent(), signParams.getCharset(),
					privateKey));
			return ret;
		} catch (Exception e) {
			LOG.warn("sign exception", e);
			return null;
		}
	}

	/**
	 * 验签
	 */
	@LogMethod
	public boolean verifySignature(VerifySignatureParams verifySignatureParams) {
		try {
			Key publicKey = signKeyManager.getPublicKey(verifySignatureParams
					.getChnid(), verifySignatureParams.getSignType(),
					verifySignatureParams.getKeyIndex());
			if (publicKey == null)
				return false;
			return signatureManager.verifySignature(
					verifySignatureParams.getSignType(),
					verifySignatureParams.getContent(),
					verifySignatureParams.getCharset(),
					verifySignatureParams.getSign(), publicKey).equals(
					SignatureResult.SUCCESS);

		} catch (Exception e) {
			LOG.warn("verifySignature exception", e);
			return false;
		}
	}

	/**
	 * 清除被缓存的Key
	 */
	@LogMethod
	public void flushKey() {
		try {
			signKeyManager.flushKey();
		} catch (Exception e) {
			LOG.warn("flushKey exception", e);
		}
	}

	public SignResult getDefaultParams(String chnid) {
		return signatureDefaultSettingManager.getDefaultSetting(chnid);
	}

	public SignKeyManager getSignKeyManager() {
		return signKeyManager;
	}

	public void setSignKeyManager(SignKeyManager signKeyManager) {
		this.signKeyManager = signKeyManager;
	}

	public SignatureManager getSignatureManager() {
		return signatureManager;
	}

	public void setSignatureManager(SignatureManager signatureManager) {
		this.signatureManager = signatureManager;
	}

	public SignatureDefaultSettingManager getSignatureDefaultSettingManager() {
		return signatureDefaultSettingManager;
	}

	public void setSignatureDefaultSettingManager(
			SignatureDefaultSettingManager signatureDefaultSettingManager) {
		this.signatureDefaultSettingManager = signatureDefaultSettingManager;
	}
}