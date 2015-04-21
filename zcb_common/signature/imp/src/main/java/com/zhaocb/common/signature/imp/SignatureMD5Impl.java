package com.zhaocb.common.signature.imp;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import com.tenpay.sm.lang.util.ExceptionUtil;

/**
 * @author eniacli
 * @author torryhong
 */
public class SignatureMD5Impl implements Signature {

	private static final Logger LOG = Logger.getLogger(SignatureMD5Impl.class);
	private final static char hexDigits[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private final String signatureAlgorithm = "MD5";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tenpay.i18npay.common.signature.Signature#sign(byte[])
	 */
	public String sign(String content, String charset, Key key) {

		try {
			String signStr = String.format("%s&key=%s", content, key
					.getFormat());
			System.out.println("signStr="+signStr);
			return getMD5(signStr, charset);
		} catch (Exception e) {
			LOG.error("生成签名出错", e);
			throw ExceptionUtil.wrapException(e);
		}

	}

	public SignatureResult verifySignature(String content, String charset,
			String sign, Key key) {
		try {
			String signStr = String.format("%s&key=%s", content, key
					.getFormat());
			String calculateSign = getMD5(signStr, charset);
			if (calculateSign != null && calculateSign.length() >= 4)
				LOG.info(String.format(
						"content:%s, sign:%s, calculateSignTail:%s", content,
						sign, calculateSign
								.substring(calculateSign.length() - 4)));
			if (sign.equalsIgnoreCase(calculateSign)) {
				return SignatureResult.SUCCESS;
			} else
				return SignatureResult.FAIL;
		} catch (Exception e) {
			LOG.error("验证签名出错", e);
			throw ExceptionUtil.wrapException(e);
		}
	}

	private String getMD5(String sourceStr, String charset)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {

		byte[] strTemp;
		if (charset == null) {
			strTemp = sourceStr.getBytes();
		} else {
			strTemp = sourceStr.getBytes(charset);
		}

		MessageDigest mdTemp = MessageDigest.getInstance(signatureAlgorithm);
		mdTemp.update(strTemp);
		byte[] md = mdTemp.digest();
		int j = md.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = md[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>>
			// 为逻辑右移，将符号位一起右移

			str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
		}
		return new String(str);
	}
	
	public static void main(String args[]){
		String content="a=b&c=d";
		String charset="GBK";
		Key key = null;
		
		key = new MD5Key("123456");
		
		Signature signature = new SignatureMD5Impl();
		System.out.println(signature.sign(content,charset,key));
	}

}
