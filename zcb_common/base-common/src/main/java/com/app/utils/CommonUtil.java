package com.app.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tenpay.sm.context.Context;
import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.lang.error.ErrorCode;
import com.tenpay.sm.web.context.WebModuleContext;
import com.tenpay.sm.web.model.AdditionalHandlerInterceptor;
import com.tenpay.sm.web.view.ViewUtil;

public class CommonUtil {

	private static final Log LOG = LogFactory.getLog(CommonUtil.class);

	/**
	 * 估计url和参数生成完整连接
	 * 
	 * @param toURL
	 * @param paras
	 * @return
	 */
	public static String genAllUrl(String toURL, String paras) {
		String allUrl = null;
		if (toURL == null) {
			throw new RuntimeException("toURL is null");
		}
		if (toURL.indexOf("?") == -1) {// 原url不带参数
			allUrl = toURL + "?" + paras;
		} else {// 原url带参数
			allUrl = toURL + "&" + paras;
		}
		return allUrl;
	}

	/**
	 * 将参数传按照给定连接符拆开 参数名称与值之间用=连接
	 * 
	 * @param src
	 * @param token
	 * @return
	 */
	public static Map<String, String> splitParaStr(String src, String token) {
		return splitParaStr(src, token, "GBK");
	}

	public static Map<String, String> splitParaStr(String src, String token,
			String charset) {
		Map<String, String> resMap = null;
		try {
			String[] items = src.split(token);
			resMap = new HashMap<String, String>();
			for (String item : items) {
				String[] paraAndValue = item.split("=", 2);
				if (paraAndValue.length > 1) {
					resMap.put(paraAndValue[0].trim(), URLDecoder.decode(
							paraAndValue[1].trim(), charset));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("illegal para str:" + src);
		}
		return resMap;
	}

	/**
	 * 将支付密码的前20位的时间种子转换为以秒为单位的时间戳
	 * 
	 * @param tamp
	 * @return
	 */
	public static String parseTmp(char[] tamp) {

		if (tamp.length != 20) {
			throw new RuntimeException("支付密码格式错误");
		}
		int i = 0;
		char bitChar[] = new char[2];
		char retTmp[] = new char[10];
		while (i < 10) {
			bitChar[0] = tamp[2 * i];
			bitChar[1] = tamp[2 * i + 1];
			retTmp[i] = (char) Integer.parseInt(String.valueOf(bitChar), 16);
			i++;
		}
		return String.valueOf(retTmp);
	}

	/**
	 * 签名参数排序
	 * 
	 * @param keysArr
	 */
	public static void signParaSort(String[] keysArr) {
		Arrays.sort(keysArr);
	}

	/**
	 * 将以&连接的参数传（queryString）格式化，以参数名称排序, 剔除空值
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String formatQueryString(String originalStr,
			boolean urlencode, String encodecharset)
			throws UnsupportedEncodingException {
		if (originalStr == null) {
			return null;
		}
		Map<String, String> paraMap = splitParaStr(originalStr, "&");
		return formatQueryParaMap(paraMap, urlencode, encodecharset);
	}

	/**
	 * 将参数map，转换为按参数名称排序的字符串, 剔除空值
	 * 
	 * @param paraMap
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String formatQueryParaMap(Map<?, ?> paraMap,
			boolean urlencode, String encodecharset)
			throws UnsupportedEncodingException {
		Set<?> keySet = paraMap.keySet();
		String[] keysArr = keySet.toArray(new String[keySet.size()]);
		signParaSort(keysArr);
		StringBuffer buff = new StringBuffer();
		for (String key : keysArr) {
			String value = trimString((String) paraMap.get(key));
			if (null != value && !"null".equalsIgnoreCase(value)) {
				if (urlencode) {
					value = URLEncoder.encode(value, encodecharset);
				}
				buff.append(key).append("=").append(value).append("&");
			}
		}
		String ret;
		if (buff.length() > 0)
			ret = buff.substring(0, buff.length() - 1);
		else
			ret = "";
		return ret;
	}

	/**
	 * 将参数map，转换为按参数名称排序的字符串, 剔除空值
	 * 
	 * @param paraMap
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String formatQueryParaMap(SortedMap<?, ?> paraMap,
			boolean urlencode, String encodecharset)
			throws UnsupportedEncodingException {
		Set<?> keySet = paraMap.keySet();
		StringBuffer buff = new StringBuffer();
		Iterator keySetIt = keySet.iterator();
		while (keySetIt.hasNext()) {
			String key = (String) keySetIt.next();
			String value = trimString((String) paraMap.get(key));
			if (null != value && !"null".equalsIgnoreCase(value)) {
				if (urlencode) {
					value = URLEncoder.encode(value, encodecharset);
				}
				buff.append(key).append("=").append(value).append("&");
			}
		}
		String ret;
		if (buff.length() > 0)
			ret = buff.substring(0, buff.length() - 1);
		else
			ret = "";
		return ret;
	}

	/**
	 * 将参数map，转换为按参数名称排序的字符串, 不剔除空值
	 * 
	 * @param paraMap
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String formatQueryParaMapWithBlank(Map<?, ?> paraMap,
			boolean urlencode, String encodecharset)
			throws UnsupportedEncodingException {
		Set<?> keySet = paraMap.keySet();
		StringBuffer buff = new StringBuffer();
		Iterator keySetIt = keySet.iterator();
		while (keySetIt.hasNext()) {
			String key = (String) keySetIt.next();
			String value = (String) paraMap.get(key);
			if (null != value) {
				if (urlencode) {
					value = URLEncoder.encode(value, encodecharset);
				}
				buff.append(key).append("=").append(value).append("&");
			}
		}

		String ret;
		if (buff.length() > 0)
			ret = buff.substring(0, buff.length() - 1);
		else
			ret = "";
		return ret;
	}

	/**
	 * trim 0长字符串转换为null
	 * 
	 * @param value
	 * @return
	 */
	public static String trimString(String value) {
		String ret = null;
		if (value != null) {
			ret = value.trim();
			if (ret.length() == 0)
				ret = null;
		}
		return ret;
	}

	/**
	 * null转换为0长字符串
	 * 
	 * @param value
	 * @return
	 */
	public static String nullToZeroLenthStr(String value) {
		value = trimString(value);
		if (value == null) {
			value = "";
		}
		return value;
	}

	/**
	 * null转换为0
	 * 
	 * @param value
	 * @return
	 */
	public static String nullToZero(String value) {
		value = trimString(value);
		if (value == null) {
			value = "0";
		}
		return value;
	}

	/**
	 * 时间格式转换
	 * 
	 * @param input
	 * @param inputFormat
	 * @param outFormat
	 * @return
	 * @throws ParseException
	 */
	public static String transDataStrFormate(String input, String inputFormat,
			String outFormat) throws ParseException {
		SimpleDateFormat inputFormate = new SimpleDateFormat(inputFormat);
		SimpleDateFormat outFormate = new SimpleDateFormat(outFormat);
		return outFormate.format(inputFormate.parse(input));
	}

	/**
	 * bean内容tostring
	 * 
	 * @param bean
	 * @return
	 */
	public static String beanToString(Object bean) {
		try {
			return BeanUtils.describe(bean).toString();
		} catch (Exception e) {
			LOG.error("beanToString error", e);
		}
		return null;
	}

	/**
	 * 获取当前线程的http request
	 * 
	 * @return
	 */
	public static HttpServletRequest getHttpServletRequest() {
		Context ctx = ContextUtil.getContext();
		if (ctx instanceof WebModuleContext) {
			WebModuleContext webContext = (WebModuleContext) ctx;
			HttpServletRequest request = webContext.getRequest();
			return request;
		}
		return null;
	}

	/**
	 * 获取当前线程的http respone
	 * 
	 * @return
	 */
	public static HttpServletResponse getHttpServletResponse() {
		Context ctx = ContextUtil.getContext();
		if (ctx instanceof WebModuleContext) {
			WebModuleContext webContext = (WebModuleContext) ctx;
			HttpServletResponse respone = webContext.getResponse();
			return respone;
		}
		return null;
	}

	/**
	 * 将错误显示在错误界面
	 * 
	 * @param errorCode
	 */
	public static void displayError(ErrorCode errorCode) {
		AdditionalHandlerInterceptor.getAdditionalModel().put("errorCode",
				errorCode);
		ViewUtil.setViewName("common/error.htm");
	}

	/**
	 * 计算时间差 开始时间不为空，则计算开始时间，否则开始时间为服务器当前时间
	 * 
	 * @param start
	 * @param timeEnd
	 * @return
	 */
	public static String getExceedTime(String start, String timeEnd,
			String timeFormate) {
		String ret = "0";
		if (CommonUtil.trimString(timeEnd) != null) {
			SimpleDateFormat timeFormat = new SimpleDateFormat(timeFormate);
			Date startDate = new Date();
			if (CommonUtil.trimString(start) != null) {// 开始时间不为空，则计算开始时间，否则开始时间为服务器当前时间
				String errorStartTimeStr = "illegal date formate:" + start
						+ ",correct is " + timeFormate;
				try {
					startDate = timeFormat.parse(start);
				} catch (ParseException e) {
					throw new RuntimeException(errorStartTimeStr);
				}
			}
			Date endDate = null;
			String errorEndDataStr = "illegal date formate:" + timeEnd
					+ ",correct is " + timeFormate;
			try {
				endDate = timeFormat.parse(timeEnd);
			} catch (ParseException e) {
				throw new RuntimeException(errorEndDataStr);
			}

			long statrMs = startDate.getTime();
			long endMs = endDate.getTime();
			if (endMs <= statrMs) {
				throw new RuntimeException("end_time is less than start_time!");
			}
			ret = String.valueOf((endMs - statrMs) / 1000);
		}
		return ret;
	}

	/**
	 * 获取当前线程调用方ip
	 * 
	 * @return
	 */
	public static String getClientIP() {
		Context ctx = ContextUtil.getContext();
		if (ctx instanceof WebModuleContext) {
			WebModuleContext webContext = (WebModuleContext) ctx;
			HttpServletRequest request = webContext.getRequest();
			return IPUtil.getIP(request);
		}
		return null;
	}

	/**
	 * 将date格式为字符串
	 * 
	 * @param date
	 * @param timeFormat
	 * @return
	 */
	public static String formatDate(Date date, String timeFormat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
		return simpleDateFormat.format(date);
	}

	/**
	 * 将字符串转换为date
	 * 
	 * @param date
	 * @param timeFormat
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String date, String timeFormat)
			throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
		return simpleDateFormat.parse(date);
	}

	/**
	 * 根据传入的毫秒间隔产生过期时间
	 */
	public static Date genExpireTime(long msDuration) {
		return new Date(new Date().getTime() + msDuration);
	}

	/**
	 * 检查时间是否已经过期
	 * 
	 * @throws ParseException
	 */
	public static boolean isTimeExpired(String time, String timeFormat)
			throws ParseException {
		if (time == null || (time = time.trim()).length() == 0)
			return false;
		SimpleDateFormat simpleDateFormat;
		if (timeFormat != null)
			simpleDateFormat = new SimpleDateFormat(timeFormat);
		else
			simpleDateFormat = new SimpleDateFormat();
		return isTimeExpired(simpleDateFormat.parse(time));
	}

	public static boolean isTimeExpired(Date date) {
		if (date == null)
			return false;
		return date.before(new Date());
	}

	/**
	 * 从流读取字符串
	 * 
	 * @param is
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String loadFromInputStreamAsString(InputStream is,
			String charset) throws IOException {
		String ret = null;
		try {
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] by = new byte[1024];
			int sz = 0;
			while ((sz = bis.read(by)) != -1) {
				bos.write(by, 0, sz);
			}
			ret = bos.toString(charset);
			return ret.trim();
		} finally {
			is.close();
		}
	}

	/**
	 * 将属性设置到对象
	 * 
	 * @param target
	 * @param paraMap
	 */
	public static void setPropToObject(Object target,
			Map<String, String> paraMap) {
		for (Entry<String, String> entry : paraMap.entrySet()) {
			String fieldName = entry.getKey();
			String value = entry.getValue();
			try {
				PropertyUtils.setProperty(target, fieldName, value);
			} catch (Exception e) {
				Class claszz = null;
				try {
					claszz = PropertyUtils.getPropertyType(target, fieldName);
					if (claszz == null) {
						int indexBegin = fieldName.lastIndexOf('_') + 1;
						if (indexBegin > 0 && indexBegin < fieldName.length()) {
							fieldName = fieldName.substring(0, indexBegin - 1);
							claszz = PropertyUtils.getPropertyType(target,
									fieldName);
						}
					}
					if (claszz != null && claszz.isAssignableFrom(List.class)) {
						List listField = (List) PropertyUtils.getProperty(
								target, fieldName);
						if (listField == null) {
							listField = new LinkedList();
							PropertyUtils.setProperty(target, fieldName,
									listField);
						}
						listField.add(value);
					}

				} catch (Exception e1) {
					continue;
				}
			}
		}
	}

	private static String cachedCurrentMachineIP = null;

	/**
	 * 获取当前机器ip(耗资源，就拿一次吧)
	 * 
	 * @return
	 */
	public static String getCurrentMachineIP() {
		if (cachedCurrentMachineIP != null)
			return cachedCurrentMachineIP;
		String IP = getLocalNetIP();
		if (IP != null) {
			cachedCurrentMachineIP = IP;
			return IP;
		}
		IP = getINetIP();
		if (IP != null) {
			cachedCurrentMachineIP = IP;
			return IP;
		}
		try {
			InetAddress addr = InetAddress.getLocalHost();
			IP = addr.getHostAddress();
			cachedCurrentMachineIP = IP;
			return IP;
		} catch (Exception e) {
			LOG.warn("getCurrentMachineIP exception:", e);
			return "127.0.0.1";
		}
	}

	public static String getINetIP() {
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface
					.getNetworkInterfaces();
			InetAddress ip = null;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> address = ni.getInetAddresses();
				while (address.hasMoreElements()) {
					ip = address.nextElement();
					if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
							&& ip.getHostAddress().indexOf(":") == -1) {// 外网IP
						if (LOG.isDebugEnabled())
							LOG.debug("getINetIP:" + ni.getName() + ";"
									+ ip.getHostAddress());
						return ip.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			LOG.warn("getINetIP ex:", e);
		}
		return null;
	}

	public static String getLocalNetIP() {
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface
					.getNetworkInterfaces();
			InetAddress ip = null;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> address = ni.getInetAddresses();
				while (address.hasMoreElements()) {
					ip = address.nextElement();
					if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
							&& ip.getHostAddress().indexOf(":") == -1) {
						// 内网IP
						if (LOG.isDebugEnabled())
							LOG.debug("getLocalNetIP:" + ni.getName() + ";"
									+ ip.getHostAddress());
						return ip.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			LOG.warn("getLocalNetIP ex:", e);
		}
		return null;
	}

	/**
	 * byte array to String.
	 * 
	 * @return byte[]
	 */
	public static String bytesToString(byte[] encrytpByte) {
		String result = "";
		for (Byte bytes : encrytpByte) {
			// System.out.print(bytes.intValue()+"\t");
			result += (char) bytes.intValue();
		}
		return result;
	}

	/**
	 * 对象平面展开成一层Map
	 * 
	 * @return SortedMap<String,?>
	 */
	public static SortedMap<String, String> ObjToPlainSortedMap(Object obj) {
		SortedMap<String, String> sortedMap = new TreeMap<String, String>();
		try {
			recursiveDescribeObj(obj, sortedMap, "");
		} catch (Exception e) {
			LOG.warn("ObjToPlainSortedMap Exception!", e);
		}
		return sortedMap;

	}

	public static int bytesToInt(byte[] bytes) {
		int num = bytes[0] & 0xFF;
		num |= ((bytes[1] << 8) & 0xFF00);
		num |= ((bytes[2] << 16) & 0xFF0000);
		num |= ((bytes[3] << 24) & 0xFF000000);
		return num;
	}

	/**
	 * 将整形转换为little endian字节数组
	 * 
	 * @param value
	 * @param length
	 *            数组长度
	 * @return
	 */
	public static byte[] intToLittleEndian(int value, int length) {
		byte[] bytes = new byte[length];
		for (int idx = 0; idx < length; ++idx) {
			bytes[idx] = (byte) ((value >> idx * 8) & 0xFF);
		}
		return bytes;
	}

	/**
	 * 将整形转换为big endian字节数组
	 * 
	 * @param value
	 * @param length
	 *            数组长度
	 * @return
	 */
	public static byte[] intToBigEndian(int value, int length) {
		byte[] bytes = new byte[length];
		int utmostRight = length - 1;
		for (int idx = 0; idx < length; ++idx) {
			bytes[utmostRight - idx] = (byte) ((value >> idx * 8) & 0xFF);
		}
		return bytes;
	}

	/**
	 * 将little endian字节数组转换为整形
	 * 
	 * @param bytes
	 * @return
	 */
	public static int littleEndianToInt(byte[] bytes) {
		int value = 0;
		int length = bytes.length;
		for (int idx = length - 1; idx >= 0; --idx) {
			value <<= 8;
			value |= bytes[idx];
		}
		return value;
	}

	/**
	 * 将big endian字节数组转换为整形
	 * 
	 * @param bytes
	 * @return
	 */
	public static int bigEndianToInt(byte[] bytes) {
		int value = 0;
		int length = bytes.length;
		for (int idx = 0; idx < length; ++idx) {
			value <<= 8;
			value |= bytes[idx];
		}
		return value;
	}

	/*
	 * 去除文件名中的特殊字符
	 * 
	 * @param String fileName
	 * 
	 * @return String fileName
	 */
	public static String trimFileName(String fileName) {
		if (fileName == null)
			return null;
		fileName = fileName.trim();
		int length = fileName.length();
		if (length == 0)
			return fileName;
		char[] charArray = fileName.toCharArray();
		for (int i = 0; i < length; i++) {
			switch (charArray[i]) {
			case '\r':
			case '\n':
			case '\\':
			case '/':
			case ':':
			case '*':
			case '?':
			case '\"':
			case '<':
			case '>':
			case '|':
				charArray[i] = '_';
			}
		}
		return String.valueOf(charArray);

	}

	/*
	 * 递归将对象的所有属性存到sorteMap中，如果Object 参数是Collection，Array,Map 存放在map中的key是
	 * keyName_index index是0,1,2,3...,如果是普通的对象，并且keyName 为空那么map中存放的key就是属性名
	 * 
	 * @param Object obj 要解析的对象
	 * 
	 * @param SortMap<String,String> 用来存放解析出来的属性
	 * 
	 * @param String keyName SortMap 中key的前缀
	 */
	private static void recursiveDescribeObj(Object obj,
			SortedMap<String, String> sortedMap, String keyName)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {

		if (obj == null) {
			return;
		}

		if (obj instanceof CharSequence || obj instanceof Number
				|| obj instanceof Character || obj instanceof Boolean
				|| obj instanceof java.util.Date) {
			sortedMap.put(keyName, obj.toString());
		} else if (obj.getClass().isArray()) {
			int size = Array.getLength(obj);
			for (int index = 0; index < size; index++) {
				Object item = Array.get(obj, index);
				recursiveDescribeObj(item, sortedMap, keyName + "_"
						+ String.valueOf(index));
			}
		} else if (obj instanceof java.util.Collection) {

			Iterator iter = ((java.util.Collection) obj).iterator();
			int index = 0;
			while (iter.hasNext()) {
				Object item = (Object) iter.next();
				recursiveDescribeObj(item, sortedMap, keyName + "_"
						+ String.valueOf(index));
				index++;
			}
		} else if (obj instanceof java.util.Map) {
			// element.sddAttribute("map",Boolean.TRUE.toString());
			Iterator iter = ((java.util.Map) obj).entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry item = (Map.Entry) iter.next();
				Object key = item.getKey();
				recursiveDescribeObj(item.getValue(), sortedMap, keyName + "_"
						+ key);
			}
		} else {
			java.beans.PropertyDescriptor pd[] = PropertyUtils
					.getPropertyDescriptors(obj);
			for (int i = 0; i < pd.length; i++) {
				if (pd[i].getReadMethod() != null
						&& pd[i].getWriteMethod() != null) {
					String pdName = pd[i].getName();
					Object value = PropertyUtils.getProperty(obj, pdName);
					if (keyName.length() > 0) {
						recursiveDescribeObj(value, sortedMap, keyName + "_"
								+ pdName);
					} else {
						recursiveDescribeObj(value, sortedMap, keyName + pdName);
					}
				}
			}

		}

	}

	/**
	 * 两BigDecimal相加
	 * 
	 * @param b1
	 * @param b2
	 * @return 和
	 */
	public static BigDecimal add(BigDecimal b1, BigDecimal b2) {
		if (b1 == null)
			return b2;
		if (b2 == null)
			return b1;
		return b1.add(b2);
	}

	/**
	 * 得到几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	public static void closeProcessStream(Process p) throws IOException {
		InputStream errorStream = p.getErrorStream();
		if (errorStream != null)
			errorStream.close();
		InputStream inputStream = p.getInputStream();
		if (inputStream != null)
			inputStream.close();
		OutputStream outputStream = p.getOutputStream();
		if (outputStream != null)
			outputStream.close();
	}

	/**
	 * 开启子进程
	 * 
	 * @param cmd
	 * @return
	 * @throws IOException
	 */
	public static Process startSimpleProcess(String cmd) throws IOException {
		Process p = Runtime.getRuntime().exec(cmd);
		closeProcessStream(p);
		return p;
	}

	/**
	 * 开启子进程
	 * 
	 * @param cmd
	 * @return
	 * @throws IOException
	 */
	public static Process startSimpleProcess(String[] cmd) throws IOException {
		Process p = Runtime.getRuntime().exec(cmd);
		closeProcessStream(p);
		return p;
	}

	/**
	 * 开启子进程
	 * 
	 * @param pb
	 * @return
	 * @throws IOException
	 */
	public static Process startSimpleProcess(ProcessBuilder pb)
			throws IOException {
		pb.redirectErrorStream(true);
		Process p = pb.start();
		closeProcessStream(p);
		return p;
	}
}
