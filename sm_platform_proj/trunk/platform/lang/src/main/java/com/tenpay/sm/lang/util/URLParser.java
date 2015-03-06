/**
 * 
 */
package com.tenpay.sm.lang.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author torryhong
 * 
 */
public class URLParser {

	public static Map<String, String> parseQueryString(String queryString,
			String charset, String spliter, String assignment)
			throws UnsupportedEncodingException {
		Map<String, String> map = new HashMap<String, String>();
		StringTokenizer st = new StringTokenizer(queryString, spliter);
		// 取得一个个get参数的key value
		while (st.hasMoreElements()) {
			String keyvalue = st.nextToken();
			int equalIndex = keyvalue.indexOf(assignment);
			if (equalIndex > 0) {
				String key = keyvalue.substring(0, equalIndex);
				String value = keyvalue.substring(equalIndex + 1);
				if (charset != null && value != null && value.length() > 0) {
					value = java.net.URLDecoder.decode(value, charset);
				}
				map.put(key, value);
			}
		}
		return map;
	}

	public static Map<String, String> parseQueryString(String queryString,
			String charset) throws UnsupportedEncodingException {
		return parseQueryString(queryString, charset, "&", "=");
	}

	public static Map<String, String> parseQueryString(String queryString) {
		try {
			return parseQueryString(queryString, null);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
}
