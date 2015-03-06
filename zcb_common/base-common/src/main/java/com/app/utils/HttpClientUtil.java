package com.app.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tenpay.sm.lang.config.ReloadableAppConfig;

/**
 * HTTP客户端
 * 
 * @author wenxing
 */
public class HttpClientUtil {
	private static final Log LOG = LogFactory.getLog(HttpClientUtil.class);

	private static final MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();
	public static final HttpClient httpClient = new HttpClient(
			multiThreadedHttpConnectionManager);
	static {
		HttpConnectionManagerParams httpConnectionManagerParams = multiThreadedHttpConnectionManager
				.getParams();
		
		/*
		String strMaxConnectionsPerHost = ReloadableAppConfig.appConfig.get("httpClient.maxConnectionsPerHost");
		String strMaxTotalConnections = ReloadableAppConfig.appConfig.get("httpClient.maxTotalConnections");
		String strSoTimeout = ReloadableAppConfig.appConfig.get("httpClient.soTimeout");
		String strConnectionTimeout = ReloadableAppConfig.appConfig.get("httpClient.connectionTimeout");
		*/
		int maxConnectionsPerHost = 100;
		int maxTotalConnections = 500;
		int soTimeout = 30000;
		int connectionTimeout = 20000;
		/*
		if (StringUtils.isNumeric(strMaxConnectionsPerHost)) {
			maxConnectionsPerHost = Integer.valueOf(strMaxConnectionsPerHost);
		}
		if (StringUtils.isNumeric(strMaxTotalConnections)) {
			maxTotalConnections = Integer.valueOf(strMaxTotalConnections);
		}
		
		if(StringUtils.isNumeric(strSoTimeout)) {
			soTimeout = Integer.valueOf(strSoTimeout);
		}
		
		if(StringUtils.isNumeric(strConnectionTimeout)) {
			connectionTimeout = Integer.valueOf(strConnectionTimeout);
		}
		*/
		httpConnectionManagerParams.setMaxTotalConnections(maxTotalConnections);
		httpConnectionManagerParams.setDefaultMaxConnectionsPerHost(maxConnectionsPerHost);
		httpConnectionManagerParams.setSoTimeout(soTimeout);
		httpConnectionManagerParams.setConnectionTimeout(connectionTimeout);

		
		/*
		String ignoreHttpsCert = CommonUtil
				.trimString(ReloadableAppConfig.appConfig
						.get("ignoreHttpsCert"));
		*/ 
		String ignoreHttpsCert= "true"; //验https证书的没搞定，先不验证
		if (ignoreHttpsCert != null && ignoreHttpsCert.equals("true")) {
			ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
			Protocol
					.registerProtocol("https", new Protocol("https", fcty, 443));
		}

		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
				30000);
		httpClient.getParams().setContentCharset("GBK");
	}

	/*
	 * 请求url，取得返回信息
	 * 
	 * @param String allUrl
	 * 
	 * @int timeout
	 * 
	 * @return String
	 */

	public static String httpClientCall(String allUrl, int timeout)
			throws HttpException, IOException {
		return httpClientCall(allUrl, timeout, "GBK");
	}

	/*
	 * 请求url，取得返回信息
	 * 
	 * @param String allUrl
	 * 
	 * @param int timeout
	 * 
	 * @param String charset 请求网页编码格式
	 * 
	 * @return String
	 */
	public static String httpClientCall(String allUrl, int timeout,
			String charset) throws HttpException, IOException {
		HttpClientCallResult httpClientCallResult = httpClientCallNeedStatus(
				allUrl, timeout, charset);
		if (httpClientCallResult == null)
			return null;
		else
			return httpClientCallResult.getRetString();

	}

	/*
	 * 请求url，取得返回信息
	 * 
	 * @param String allUrl
	 * 
	 * @param int timeout
	 * 
	 * @return HttpClientCallResult 包装了请求返回的状态
	 */
	public static HttpClientCallResult httpClientCallNeedStatus(String allUrl,
			int timeout) throws HttpException, IOException {
		return httpClientCallNeedStatus(allUrl, timeout, "GBK");
	}

	/*
	 * 请求url，取得返回信息
	 * 
	 * @param String allUrl
	 * 
	 * @param int timeout
	 * 
	 * @param String charset 请求网页编码格式
	 * 
	 * @return HttpClientCallResult 包装了请求返回的状态
	 */
	public static HttpClientCallResult httpClientCallNeedStatus(String allUrl,
			int timeout, String charset) throws HttpException, IOException {
		LOG.info("call url: " + allUrl);
		GetMethod get = new GetMethod(allUrl);
		get.getParams().setContentCharset(charset);
		get.setRequestHeader("Connection", "close");
		String ignoreHttpsCert = CommonUtil
				.trimString(ReloadableAppConfig.appConfig
						.get("ignoreHttpsCert"));
		if (ignoreHttpsCert != null && ignoreHttpsCert.equals("true")) {
			ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
			Protocol
					.registerProtocol("https", new Protocol("https", fcty, 443));
		}
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
				timeout);
		get.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, timeout);
		HttpClientCallResult httpClientCallResult = new HttpClientCallResult();
		try {
			int statusCode = httpClient.executeMethod(get);
			httpClientCallResult.setHttpStatus(statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				LOG.warn("HttpClient excuteMethod failed. Status: "
						+ statusCode);
				return httpClientCallResult;
			}
			httpClientCallResult.setRetString(new String(get.getResponseBody(),
					charset));
		} finally {
			get.releaseConnection();
		}
		LOG.info("call url result: "
				+ httpClientCallResult.getRetString());

		return httpClientCallResult;
	}

	/*
	 * 请求url，取得返回信息
	 * 
	 * @param String toURL
	 * 
	 * @param String signedStr
	 * 
	 * @param int timeout
	 * 
	 * @return Map<String,String>
	 */
	public static Map<String, String> httpClientCall(String toURL,
			String signedStr, int timeout) {
		return httpClientCall(toURL, signedStr, "GBK", timeout);
	}

	/*
	 * 请求url，取得返回信息
	 * 
	 * @param String toURL
	 * 
	 * @param String signedStr
	 * 
	 * @param int timeout
	 * 
	 * @param String xmlEncoding 请求网页编码格式
	 * 
	 * @return Map<String,String>
	 */
	public static Map<String, String> httpClientCall(String toURL,
			String signedStr, String xmlEncoding, int timeout) {
		String allUrl = toURL;
		if (signedStr != null)
			allUrl = allUrl + "?" + signedStr;

		LOG.info("call url: " + allUrl);
		GetMethod get = new GetMethod(allUrl);
		get.getParams().setContentCharset(xmlEncoding);
		get.setRequestHeader("Connection", "close");
		String ignoreHttpsCert = CommonUtil
				.trimString(ReloadableAppConfig.appConfig
						.get("ignoreHttpsCert"));
		if (ignoreHttpsCert != null && ignoreHttpsCert.equals("true")) {
			ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
			Protocol
					.registerProtocol("https", new Protocol("https", fcty, 443));
		}
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
				timeout);
		get.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, timeout);
		Map<String, String> resultMap = null;
		try {
			int statusCode = httpClient.executeMethod(get);
			if (statusCode != HttpStatus.SC_OK) {
				LOG.warn("HttpClient excuteMethod failed. Status: "
						+ statusCode);
				return null;
			}
			InputStream responseBody = get.getResponseBodyAsStream();
			// deal response
			BufferedReader br = new BufferedReader(new InputStreamReader(
					responseBody, xmlEncoding));
			resultMap = XmlParseUtil.xmlToMap(br);
			responseBody.close();
		} catch (Exception e) {
			LOG.warn("httpClientCall exception", e);
		} finally {
			get.releaseConnection();
		}
		if (resultMap != null) {
			LOG.info("call url result: "
					+ resultMap.toString());
		}
		return resultMap;
	}
	
	//测试方法
	public static void main(String[] args) throws HttpException, IOException, InterruptedException {
		List<Thread> threadList = new ArrayList<Thread>();
		int threadCount = 100;
		final int preThreadRunCount = 100;
		long total_starttime = System.currentTimeMillis();
		for(int i = 0; i < threadCount; i++) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					BufferedWriter writer = null;
					try {
						writer = new BufferedWriter(new FileWriter(new File("C:\\Users\\john\\Desktop\\test\\" + Thread.currentThread().getId() + ".txt")));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					long starttime = System.currentTimeMillis();
					long endtime = 0L;
					for(int j = 0; j < preThreadRunCount; j++) {
						String url = "http://www.qq.com";
						String page = "";
						try {
							GetMethod post = new GetMethod(url);
							//RequestEntity requestEntity = new StringRequestEntity(paramStr);
							//post.setRequestEntity(requestEntity);
							post.getParams().setContentCharset("UTF-8");
							post.setRequestHeader("Connection", "close");
							post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 30000);
							try {
								int statusCode = HttpClientUtil.httpClient.executeMethod(post);
								if (statusCode != HttpStatus.SC_OK) {
									throw new RuntimeException("Invalid http status:" + statusCode);
								} else
									page = post.getResponseBodyAsString();
							} finally {
								post.releaseConnection();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						if(j % (preThreadRunCount/100) == 0) {
							endtime = System.currentTimeMillis();
							System.out.println("current Thread id:" + Thread.currentThread().getId() + " run " + (preThreadRunCount) +" times, cost:" + (endtime - starttime) + "ms.");
							starttime = endtime;
						}
					}
					try {
						writer.flush();
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			});
			t.start();
			threadList.add(t);
		}
		
		for(Thread t : threadList) {
			t.join();
		}
		long total_endtime = System.currentTimeMillis();
		System.out.println("total run " + threadCount * preThreadRunCount + " times, cost:" + (total_endtime - total_starttime) + "ms.");
	}

}
