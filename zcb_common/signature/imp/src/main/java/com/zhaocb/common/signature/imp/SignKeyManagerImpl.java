/**
 * 
 */
package com.zhaocb.common.signature.imp;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.map.LRUMap;


import com.zhaocb.common.signature.dao.MerchantAccessProfileDAO;
import com.zhaocb.common.signature.po.MerchantRequestKeyPO;
import com.zhaocb.common.signature.po.TenpayResponeKeyPO;
import com.tenpay.sm.lang.config.ReloadableAppConfig;

/**
 * @author eniacli 签名的密钥管理，目前只支持rsa的 .pem, .pk8文件
 */
public class SignKeyManagerImpl implements SignKeyManager {
	private Map<String, KeyContainer> privateKeys;
	private Map<String, KeyContainer> publicKeys;
	private long expireDuration = 1800000;

	private MerchantAccessProfileDAO merchantAccessProfileDAO;

	@SuppressWarnings("unchecked")
	public SignKeyManagerImpl() {
		int signatureCacheSize = 100000;
		String signatureCacheSizeStr = ReloadableAppConfig.appConfig
				.get("signature.cacheSize");
		if (signatureCacheSizeStr != null) {
			signatureCacheSize = Integer.valueOf(signatureCacheSizeStr.trim());
		}
		privateKeys = java.util.Collections.synchronizedMap(new LRUMap(
				signatureCacheSize));
		publicKeys = java.util.Collections.synchronizedMap(new LRUMap(
				signatureCacheSize));

		String expireDurationStr = ReloadableAppConfig.appConfig
				.get("signature.expireDuration");
		if (expireDurationStr != null) {
			expireDuration = expireDuration
					+ Integer.valueOf(expireDurationStr.trim()) * 60 * 1000;
		}

	}

	public Key getPublicKey(String chnid, String signType, int keyIndex) {
		String keyPersistName = genKeyPersistName(chnid, signType, keyIndex);
		KeyContainer kc = publicKeys.get(keyPersistName);
		if (kc == null || !kc.isValid()) {
			if (kc == null)
				kc = new KeyContainer();
			if (signType.equalsIgnoreCase(SignatureType.RSA.name())) {
				MerchantRequestKeyPO merchantRequestKeyPO = null;
				try {
					merchantRequestKeyPO = merchantAccessProfileDAO
							.selectMerchantMechantRequestKeyByMerIDAndMOrderID(
									chnid, keyIndex);
					if (merchantRequestKeyPO == null) {
						return kc.getKey();
					}
					kc.setKey(genRSAPublicKey(merchantRequestKeyPO
							.getRequestPublicKey()));
				} catch (Exception e) {
					return kc.getKey();

				}
			} else {
				String keyStr;
				try {
					keyStr = merchantAccessProfileDAO.getMerchantMD5Key(chnid);
				} catch (Exception e) {
					return kc.getKey();
				}
				if(keyStr==null)
					return kc.getKey();
				kc.setKey(new MD5Key(keyStr));
				privateKeys.put(keyPersistName, kc);
			}
			publicKeys.put(keyPersistName, kc);
			Date currentDate = new Date();
			currentDate.setTime(currentDate.getTime() + expireDuration);
			kc.setExpireTime(currentDate);
		}
		return kc.getKey();
	}

	public Key getPrivateKey(String chnid, String signType, int keyIndex) {
		String keyPersistName = genKeyPersistName(chnid, signType, keyIndex);
		KeyContainer kc = privateKeys.get(keyPersistName);
		if (kc == null || !kc.isValid()) {
			if (kc == null)
				kc = new KeyContainer();
			if (signType.equalsIgnoreCase(SignatureType.RSA.name())) {
				TenpayResponeKeyPO tenpayResponeKeyPO = null;
				try {
					tenpayResponeKeyPO = merchantAccessProfileDAO
							.selectTenpayResponeKeyByerchantID(chnid);
					if (tenpayResponeKeyPO == null) {
						return kc.getKey();
					}
					kc.setKey(genRSAPrivateKey(tenpayResponeKeyPO
							.getResponePrivateKey()));
				} catch (Exception e) {
					return kc.getKey();
				}
			} else {
				String keyStr;
				try {
					keyStr = merchantAccessProfileDAO.getMerchantMD5Key(chnid);
				} catch (Exception e) {
					return kc.getKey();
				}
				if(keyStr==null)
					return kc.getKey();
				kc.setKey(new MD5Key(keyStr));
				privateKeys.put(keyPersistName, kc);
			}
			privateKeys.put(keyPersistName, kc);
			Date currentDate = new Date();
			currentDate.setTime(currentDate.getTime() + expireDuration);
			kc.setExpireTime(currentDate);
		}
		return kc.getKey();
	}

	public void flushKey() {
		privateKeys.clear();
		publicKeys.clear();
	}

	private String genKeyPersistName(String chnid, String signType, int keyIndex) {
		return chnid + "_" + signType + "_" + String.valueOf(keyIndex);
	}

	/**
	 * 用密钥base64字符串生成rsa公钥
	 * 
	 * @param base64PubKey
	 * @return
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	private PublicKey genRSAPublicKey(String base64PubKey)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		byte[] keyBytes = Base64.decodeBase64(base64PubKey.getBytes());
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
		return publicKey;
	}

	/**
	 * 用密钥base64字符串生成rsa私钥
	 * 
	 * @param base64PubKey
	 * @return
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	private PrivateKey genRSAPrivateKey(String base64PriKey)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		byte[] keyBytes = Base64.decodeBase64(base64PriKey.getBytes());
		PKCS8EncodedKeySpec privatePKCS8 = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(privatePKCS8);
		return privateKey;
	}

	public MerchantAccessProfileDAO getMerchantAccessProfileDAO() {
		return merchantAccessProfileDAO;
	}

	public void setMerchantAccessProfileDAO(
			MerchantAccessProfileDAO merchantAccessProfileDAO) {
		this.merchantAccessProfileDAO = merchantAccessProfileDAO;
	}
}
