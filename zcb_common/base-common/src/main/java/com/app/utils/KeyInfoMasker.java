package com.app.utils;


import java.util.StringTokenizer;

import com.tenpay.sm.lang.config.ReloadableAppConfig;

public class KeyInfoMasker {
	private static String[] keyArray = new String[0];
	private static String[] maskArray = new String[0];
	private static int keyCount = 0;
	static {
		try {
			String maskKeys = CommonUtil
					.trimString(ReloadableAppConfig.appConfig
							.get("need_mask_keys"));
			if (maskKeys != null) {
				StringTokenizer st = new StringTokenizer(maskKeys, ";");
				int count = st.countTokens();
				keyArray = new String[count];
				maskArray = new String[count];
				int index = 0;
				while (st.hasMoreTokens()) {
					String maskKeyWithLength = CommonUtil.trimString(st
							.nextToken());
					if (maskKeyWithLength != null) {
						try {
							String[] maskKeyWithLengthStrs = maskKeyWithLength
									.split(",");
							keyArray[index] = CommonUtil
									.trimString(maskKeyWithLengthStrs[0]);
							if (maskKeyWithLengthStrs.length > 1) {
								int length = Integer
										.valueOf(maskKeyWithLengthStrs[1]);
								char[] maskChars = new char[length];
								for (int i = 0; i < maskChars.length; i++) {
									maskChars[i] = '*';
								}
								maskArray[index] = new String(maskChars);
							} else {
								maskArray[index] = "*";
							}
						} catch (Throwable t2) {
						}
					}
					index++;
				}
			}
		} catch (Throwable t) {
		}
		keyCount = keyArray.length;
	}

	public static String maskKeyInfo(String src) {
		if (src == null)
			return src;

		// 检查mask关键信息
		for (int i = 0; i < keyCount; i++) {
			String key = keyArray[i];
			if (key == null)
				continue;
			try {
				int index = src.indexOf(key);
				if (index >= 0) {
					int endIndex = index + key.length() + 1
							+ maskArray[i].length();
					if (endIndex > src.length())
						endIndex = src.length();
					String toReplace = src.substring(index, endIndex);
					endIndex = key.length() + 1;
					if (endIndex > toReplace.length())
						endIndex = toReplace.length();
					src = src.replace(toReplace, toReplace.substring(0,
							endIndex)
							+ maskArray[i]);
				}
			} catch (Throwable t) {
			}
		}
		return src;
	}
}
