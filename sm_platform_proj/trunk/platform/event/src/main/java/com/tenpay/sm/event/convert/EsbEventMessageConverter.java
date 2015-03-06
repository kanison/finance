/**
 * 
 */
package com.tenpay.sm.event.convert;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import com.tenpay.sm.event.EsbEvent;

/**
 * @author Administrator
 *
 */
public class EsbEventMessageConverter implements MessageConverter {

	/* (non-Javadoc)
	 * @see org.springframework.jms.support.converter.MessageConverter#fromMessage(javax.jms.Message)
	 */
	public Object fromMessage(Message message) throws JMSException,
			MessageConversionException {
		MapMessage mapMsg = (MapMessage) message;
		EsbEvent esbEvent = new EsbEvent();
		esbEvent.setEventId(mapMsg.getString("eventId"));
		esbEvent.setEventType(mapMsg.getString("eventType"));
		esbEvent.setEventData(mapMsg.getString("eventData"));
		return esbEvent;
	}

	/* (non-Javadoc)
	 * @see org.springframework.jms.support.converter.MessageConverter#toMessage(java.lang.Object, javax.jms.Session)
	 */
	public Message toMessage(Object object, Session session)
			throws JMSException, MessageConversionException {
		EsbEvent event = (EsbEvent)object;
		MapMessage mapMessage =  session.createMapMessage();
		mapMessage.setString("eventType", event.getEventType());
		/**
		 * Éú³ÉÄ¬ÈÏEventId
		 */
		mapMessage.setString("eventId", event.getEventId());
		mapMessage.setString("eventData", event.getEventData());
		return mapMessage;
	}

}
