package com.zhaocb.common.aop.aspect.checklogin.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.app.utils.CommonUtil;
import com.tenpay.sm.lang.config.ReloadableAppConfig;
import com.zhaocb.common.aop.aspect.checklogin.TenpaySessionClient;
/**
 * TenpaySessionClientImpl
 */
public class TenpaySessionClientImpl implements TenpaySessionClient {
	private static final Log LOG = LogFactory.getLog(TenpaySessionClientImpl.class);

	public static String TENPAY_SESSION_SERVER_IP = "TENPAY_SESSION_SERVER_IP";
	public static String TENPAY_SESSION_SERVER_PORT = "TENPAY_SESSION_SERVER_PORT";
	public static String TENPAY_SESSION_SERVER_TIME_OUT = "TENPAY_SESSION_SERVER_TIME_OUT";
	public static int DEFAULT_TENPAY_SESSION_SERVER_TIME_OUT = 2000;
	public static int MAX_LENGTH_OF_RECEIVE_BUFFER = 1564;
	public static String QPAY_LOGIN_SESSION_NAME = "ql_sess"; // 不能超过10个字节


	private String sessionName = QPAY_LOGIN_SESSION_NAME;
	/**
	 * getSession
	 * @param sessionKey
	 * @return byte[]
	 * @throws Exception 
	 */
	public byte[] getSession(String sessionKey) throws Exception {
		if (sessionKey == null) {
			throw new Exception("getSession参数不正确,没有有效的sessionKey");
		}
		int timeout = DEFAULT_TENPAY_SESSION_SERVER_TIME_OUT;
		try {
			timeout = Integer.parseInt(CommonUtil
					.trimString(ReloadableAppConfig.appConfig.get(TENPAY_SESSION_SERVER_TIME_OUT)));
		} catch (Exception e) {
			LOG.warn("SessionClient调用超时时间没有配置，用默认时间" + DEFAULT_TENPAY_SESSION_SERVER_TIME_OUT);
		}
		String serverIp = CommonUtil
		.trimString(ReloadableAppConfig.appConfig.get(TENPAY_SESSION_SERVER_IP));
		int serverPort = Integer.parseInt(CommonUtil
				.trimString(ReloadableAppConfig.appConfig.get(TENPAY_SESSION_SERVER_PORT)));
		//udp通讯
		DatagramSocket datagramSocket = null;
		try {
			byte[] data = SessionClientUtil.packDataBytes(sessionName,sessionKey);
			datagramSocket = new DatagramSocket();
			datagramSocket.setSoTimeout(timeout);
			DatagramPacket dp = new DatagramPacket(data, data.length, new InetSocketAddress(serverIp, serverPort));
			byte[] buffer = new byte[MAX_LENGTH_OF_RECEIVE_BUFFER];
			DatagramPacket resultdp = new DatagramPacket(buffer, buffer.length);
			try {
				datagramSocket.send(dp);
				datagramSocket.receive(resultdp);
			} catch (IOException e2) {
				//如果收发失败，重试一次
				LOG.info("Retry comunicate with session server: "+CommonUtil
						.trimString(ReloadableAppConfig.appConfig.get(TENPAY_SESSION_SERVER_IP)));
				datagramSocket.send(dp);
				datagramSocket.receive(resultdp);
			}
			return SessionClientUtil.unpackDataBytes(resultdp.getData());
		} catch (IOException e) {
			LOG.warn("SessionClient exception!", e);
			throw e;
		} finally {
			if (datagramSocket != null)
				datagramSocket.close();
		}
	}

	public String getSessionName() {
		return sessionName;
	}
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
}
