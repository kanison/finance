package com.app.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	private final static char hexDigits[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/*
	 * 获取字符串md5
	 * @param sourceStr
	 * @param charset
	 * @return md5Str
	 * */
	public static String getMD5(String sourceStr, String charset)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		byte[] strTemp;
		if (charset == null) {
			strTemp = sourceStr.getBytes();
		} else {
			strTemp = sourceStr.getBytes(charset);
		}

		MessageDigest mdTemp = MessageDigest.getInstance("MD5");
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
}
