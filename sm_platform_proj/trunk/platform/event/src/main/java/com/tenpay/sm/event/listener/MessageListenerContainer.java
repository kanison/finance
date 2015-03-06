package com.tenpay.sm.event.listener;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.listener.SimpleMessageListenerContainer;

/**
 * @author fortimefan
 * 
 */
public class MessageListenerContainer extends SimpleMessageListenerContainer {
	private static final Log LOG = LogFactory
			.getLog(MessageListenerContainer.class);

	/**
	 * Same as AbstractJmsListeningContainer's, but it won't throw JmsException.
	 * Instead, it will send a warn message.
	 */
	public void initialize() {
		try {
			if (this.isSubscriptionDurable() && this.getClientId() == null) {
				// 设置默认的clientId
				String ip;
				try {
					ip = getNetIP();
				} catch (Exception e) {
					LOG.warn("getCurrentMachineIP exception:", e);
					ip = "127.0.0.1";
				}
				this.setClientId(ip + this.getDurableSubscriptionName());
			}
			super.initialize();
		} catch (JmsException jmsException) {
			LOG.warn("启动Activemq的接收器出错，请留意Activemq的启动情况", jmsException);
		}
	}

	private static String getNetIP() {
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface
					.getNetworkInterfaces();
			InetAddress ip;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> address = ni.getInetAddresses();
				while (address.hasMoreElements()) {
					ip = address.nextElement();
					if (!ip.isLoopbackAddress()
							&& ip.getHostAddress().indexOf(":") == -1) {
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
		return "127.0.0.1";
	}
}
