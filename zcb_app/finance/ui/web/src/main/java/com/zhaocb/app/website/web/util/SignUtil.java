package com.zhaocb.app.website.web.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * 
 * @author zhl
 *
 */
public class SignUtil {
	public static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static void main(String[] args) throws Exception {
		String fileName = "D:\\mchapi.zip";

		String hashType = "MD5";
		String str1 = getHash(fileName, hashType);
		System.out.println(hashType + " == " + str1);

		readFile(fileName);

		fileName = "D:\\zcb\\tools.rar";
		String str2 = getHash(fileName, hashType);
		System.out.println(hashType + " == " + str2);

		System.out.println(str1.equals(str2));

	}

	public static String getHash(String fileName, String hashType) {
		InputStream fis;
		MessageDigest md5;
		try {
			fis = new FileInputStream(fileName);
			byte[] buffer = new byte[1024];
			md5 = MessageDigest.getInstance(hashType);

			int numRead = 0;
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return toHexString(md5.digest());
	}

	public static void readFile(String fileName) throws Exception {
		InputStream fis;
		fis = new FileInputStream(fileName);
		byte[] buffer = new byte[1024];
		while ((fis.read(buffer)) > 0) {

		}
		fis.close();
		return;
	}

	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString().toUpperCase();
	}
}