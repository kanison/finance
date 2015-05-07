package com.zhaocb.zcb_app.finance.fep.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

/**
 * 连接类
 * 
 * @author zhl
 *
 */
public class ConnectionUtil {

	/** 超时时间,以秒为单位 */
	private static final int timeOut = 30;

	private static final String USER_AGENT_VALUE = "Mozilla/4.0 (compatible; MSIE 6.0; Windows XP)";

	private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";

	/**
	 * 
	 * @param strUrl
	 * @param sslContext
	 * @return
	 * @throws Exception
	 */
	public static HttpsURLConnection getHttpsURLConnection(String strUrl,
			SSLContext sslContext) throws Exception {
		URL url = new URL(strUrl);
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				return true;
			}
		};

		HttpsURLConnection.setDefaultHostnameVerifier(hv);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setSSLSocketFactory(sslContext.getSocketFactory());

		// 以post方式通信
		conn.setRequestMethod("POST");

		// 设置请求默认属性
		setHttpRequest(conn);

		conn.connect();

		return conn;
	}

	/**
	 * 设置http请求默认属性
	 * 
	 * @param httpConnection
	 */
	public static void setHttpRequest(HttpURLConnection conn) {

		// 设置连接超时时间
		conn.setConnectTimeout(timeOut * 1000);

		// User-Agent
		conn.setRequestProperty("User-Agent", USER_AGENT_VALUE);

		// 不使用缓存
		conn.setUseCaches(false);

		// 允许输入输出
		conn.setDoInput(true);
		conn.setDoOutput(true);

		// Content-Type
		conn.setRequestProperty("Content-Type", CONTENT_TYPE);

	}

	/**
	 * 处理输出<br/>
	 * 注意:流关闭需要自行处理
	 * 
	 * @param out
	 * @param data
	 * @param len
	 * @throws IOException
	 */
	public static void doOutput(OutputStream out, byte[] data, int len)
			throws IOException {
		int dataLen = data.length;
		int off = 0;
		while (off < data.length) {
			if (len >= dataLen) {
				out.write(data, off, dataLen);
				off += dataLen;
			} else {
				out.write(data, off, len);
				off += len;
				dataLen -= len;
			}

			// 刷新缓冲区
			out.flush();
		}

	}

	/**
	 * InputStream转换成Byte 注意:流关闭需要自行处理
	 * 
	 * @param in
	 * @return byte
	 * @throws Exception
	 */
	public static byte[] InputStreamTOByte(InputStream in) throws IOException {
		int BUFFER_SIZE = 4096;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;

		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;
		byte[] outByte = outStream.toByteArray();
		outStream.close();

		return outByte;
	}

	/**
	 * InputStream转换成String 注意:流关闭需要自行处理
	 * 
	 * @param in
	 * @param encoding
	 *            编码
	 * @return String
	 * @throws Exception
	 */
	public static String InputStreamTOString(InputStream in, String encoding)
			throws IOException {
		return new String(InputStreamTOByte(in), encoding);
	}
}
