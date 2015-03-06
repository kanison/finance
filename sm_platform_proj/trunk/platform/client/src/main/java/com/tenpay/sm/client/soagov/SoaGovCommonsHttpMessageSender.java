/**
 * 
 */
package com.tenpay.sm.client.soagov;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.XFireException;
import org.codehaus.xfire.exchange.OutMessage;
import org.codehaus.xfire.transport.http.CommonsHttpMessageSender;

import com.tenpay.sm.lang.util.Base64;

/**
 * @author torryhong
 *
 */
public class SoaGovCommonsHttpMessageSender extends CommonsHttpMessageSender {
	private static final Log log = LogFactory.getLog(SoaGovCommonsHttpMessageSender.class);
	
	public SoaGovCommonsHttpMessageSender(OutMessage message, MessageContext context) {
		super(message, context);
	}

	@Override
	public void send() throws HttpException, IOException, XFireException {
		String responseHeader = null;
		try {
			super.send();
			try {
				responseHeader = this.getMethod().getResponseHeader(
						RequestHeaderXFireSoaGovInterceptor.KEY_SOAGOV_INVOKE_RESPONSEHEADER).getValue();
				responseHeader = Base64.fromBase64(responseHeader);
				//responseHeader = responseHeader.replace("\n", "\n------");
				responseHeader = responseHeader.replace("   ", "&nbsp;&nbsp;&nbsp;");
			} catch(Exception ex) {
				log.warn("获得soa响应头出错",ex);
			}
		} finally {
			RequestHeaderXFireSoaGovInterceptor.setInvokeSoaGovResponseHeader(responseHeader);
		}
	}
	
}
