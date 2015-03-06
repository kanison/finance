/**
 * 
 */
package com.tenpay.sm.test.esbevent;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.jms.listener.SessionAwareMessageListener;

import com.tenpay.sm.event.EsbEvent;
import com.tenpay.sm.event.listener.EsbEventListener;

/**
 * @author Administrator
 *
 */
public class MyEsbEventListener implements EsbEventListener, MessageListener,SessionAwareMessageListener  {
	/* (non-Javadoc)
	 * @see com.tenpay.sm.event.listener.EsbEventListener#onEvent(com.tenpay.sm.event.EsbEvent)
	 */
	public void onEvent(EsbEvent esbEvent) {
		System.out.println(new java.util.Date() + " MyEsbEventListener 收到消息");
		System.out.println(ToStringBuilder.reflectionToString(esbEvent));
	}
	
	public void myreceive(MapMessage mapMsg){
		this.onMessage(mapMsg);
	}
	
	public void onMessage(Message message) {
		try {
			MapMessage mapMsg = (MapMessage) message;
			EsbEvent esbEvent = new EsbEvent();
			esbEvent.setEventId(mapMsg.getString("eventId"));
			esbEvent.setEventType(mapMsg.getString("eventType"));
			esbEvent.setEventData(mapMsg.getString("eventData"));
			this.onEvent(esbEvent);
		} catch (JMSException e) {
			throw new RuntimeException("处理消息出错" + message,e);
		}
//		try {
//			message.acknowledge();
//		} catch (JMSException e) {
//			throw new RuntimeException("消息确认出错" + message,e);
//		}
	}

	public void onMessage(Message message, Session session) throws JMSException {
		this.onMessage(message);
	}

}
