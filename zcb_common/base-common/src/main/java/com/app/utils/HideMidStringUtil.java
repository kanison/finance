package com.app.utils;



/**
 * 隐藏指定字符串中间几位
 * 
 */

public class HideMidStringUtil {

	// 定义异常
	public static class HideStringException extends RuntimeException {
		private static final long serialVersionUID = -2049456931429416858L;

		public HideStringException() {
			super();
		}

		public HideStringException(String msg) {
			super(msg);
		}

		public HideStringException(String msg, Throwable cause) {
			super(msg, cause);
		};
	}

	/**
	 * 隐藏指定字符串中间几位
	 * 
	 * @param sSrc
	 *            : 原始串 iStart: 前面显示位数 iEnd: 后面显示位数
	 *@return HideMidStr("123456ur789", 3, 4), 结果: 123****r789
	 */
	public static String hideMidString(String sSrc, int iStart, int iEnd){
		if (sSrc == null)
			return null;
		if (iStart<0||sSrc.length() <= iStart)
			return sSrc;
		char[] charArray = sSrc.toCharArray();
		int arrayLength = charArray.length;
		int hopeEnd = arrayLength - iEnd;
		if (hopeEnd < iStart)
			return sSrc;
		for (int i = iStart; i < hopeEnd; i++) {
			charArray[i] = '*';
		}
		return new String(charArray);
	}
	
	public static String hideCreId(String creId) {
		if (CommonUtil.trimString(creId) == null)
			return creId;
		return HideMidStringUtil.hideMidString(creId, 6, 2);
	}

	public static String hideMobile(String mobile) {
		if (CommonUtil.trimString(mobile) == null)
			return mobile;
		return HideMidStringUtil.hideMidString(mobile, 3, 4);
	}

}
