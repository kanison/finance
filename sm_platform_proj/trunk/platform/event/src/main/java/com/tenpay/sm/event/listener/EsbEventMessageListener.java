/**
 * 
 */
package com.tenpay.sm.event.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import com.tenpay.sm.common.lang.diagnostic.Profiler;
import com.tenpay.sm.event.EsbEvent;
import com.tenpay.sm.event.convert.EsbEventMessageConverter;
import com.tenpay.sm.lang.log.Loggers;

/**
 * @author Administrator
 *
 */
public class EsbEventMessageListener implements MessageListener,SessionAwareMessageListener {
	private MessageConverter messageConverter = new EsbEventMessageConverter();
	private EsbEventListener esbEventListener;
	
	public void onMessage(Message message) {
		Profiler.reset();
		Profiler.start("消息处理开始执行");
		Object object;
		try {
			Profiler.enter("消息转换开始");
			object = messageConverter.fromMessage(message);
			Profiler.release();
		} catch (Exception e) {
			throw new RuntimeException("转换消息出错," + message,e);
		} 
		if(object!=null && esbEventListener!=null && object instanceof EsbEvent) {
			Profiler.enter("事件处理");
			esbEventListener.onEvent((EsbEvent)object);
			Profiler.release();
		}
		Profiler.release();
		if(Profiler.getDuration()>600) {
			Loggers.PERF.warn("消息处理执行时间超过阈值：" + Profiler.getDuration() + Profiler.dump());
		}
	}
	
	public void onMessage(Message message, Session session) throws JMSException {
		this.onMessage(message);
		/**
		 * TODO something with session
		 */
	}


	/**
	 * @return the esbEventListener
	 */
	public EsbEventListener getEsbEventListener() {
		return esbEventListener;
	}

	/**
	 * @param esbEventListener the esbEventListener to set
	 */
	public void setEsbEventListener(EsbEventListener esbEventListener) {
		this.esbEventListener = esbEventListener;
	}

	/**
	 * @return the messageConverter
	 */
	public MessageConverter getMessageConverter() {
		return messageConverter;
	}

	/**
	 * @param messageConverter the messageConverter to set
	 */
	public void setMessageConverter(MessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}

}
