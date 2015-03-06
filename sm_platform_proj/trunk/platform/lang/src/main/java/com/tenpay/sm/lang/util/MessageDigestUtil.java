/**
 * 
 */
package com.tenpay.sm.lang.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 使用md5,sha等方式生成hash散列的工具，默认SHA-256
 * @author li.hongtl
 *
 */
public class MessageDigestUtil {
	public static final String ALGORITHM = "SHA-256";
	/**
	 * 使用默认算法生成摘要
	 * @param content
	 * @return
	 */
	public static String digest(String content) {
		try {
			return digest(content,ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	/**
	 * 生成摘要
	 * @param content
	 * @param messageDigestAlgorithm
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String digest(String content,String messageDigestAlgorithm) throws NoSuchAlgorithmException {
		byte[] defaultBytes = content.getBytes();
		MessageDigest algorithm = MessageDigest.getInstance(messageDigestAlgorithm);
		algorithm.reset();
		algorithm.update(defaultBytes);
		byte messageDigest[] = algorithm.digest();

//		java.math.BigInteger number = new java.math.BigInteger(1, messageDigest);
//		return number.toString(16).toUpperCase();
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < messageDigest.length; i++) {
			hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
		}
        if (hexString.length() == 1)
        {
            hexString.append('0');
        }
		return hexString.toString();
	}
	
}
