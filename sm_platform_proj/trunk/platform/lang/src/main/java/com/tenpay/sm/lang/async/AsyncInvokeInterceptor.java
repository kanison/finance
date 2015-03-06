/**
 * 
 */
package com.tenpay.sm.lang.async;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.tenpay.sm.common.lang.diagnostic.Profiler;
import com.tenpay.sm.lang.log.Loggers;
/**
 * @author torryhong
 * �첽ִ�з����ķ���������
 */
public class AsyncInvokeInterceptor implements MethodInterceptor {
	private static final Logger logger = Logger.getLogger(AsyncInvokeInterceptor.class);
	private boolean afterTransactionCommit = true;
	private TaskExecutor taskExecutor;
	
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		 if (afterTransactionCommit && TransactionSynchronizationManager.isActualTransactionActive()) {
	        if (logger.isInfoEnabled()) {
				logger.info("���ڴ��������У��ݻ������첽ִ�У�ע������ͬ������");
			}
			TransactionSynchronizationManager
					.registerSynchronization(new TransactionSynchronizationAdapter() {
						public void afterCommit() {
							AsyncInvokeInterceptor.this.startAsyncInvoke(invocation);
						}
					});
		 } 
		 else {
			 this.startAsyncInvoke(invocation);
		 }
		//WARN�������ܵõ�����ֵ
		return null;
	}
	
	public void startAsyncInvoke(final MethodInvocation invocation) {
		taskExecutor.execute(
			new java.lang.Runnable() {
				public void run() {
					Profiler.reset();
					Profiler.start("�첽���ÿ�ʼִ��" + invocation.getThis().getClass() + "." + invocation.getMethod().getName());
					try {
						Object returnValue = invocation.proceed();
						if(logger.isInfoEnabled()) {
							logger.debug("ִ��������ɣ����أ�" + returnValue);
						}
					} catch (Throwable e) {
						logger.error("����ִ�г��쳣",e);
					}
					Profiler.release();
					if(Profiler.getDuration()>600) {
						Loggers.PERF.warn("ִ��ʱ�䳬����ֵ��" + Profiler.getDuration() + Profiler.dump());
					}
				}
			}
		);		
	}
	
	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}
	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public boolean isAfterTransactionCommit() {
		return afterTransactionCommit;
	}

	public void setAfterTransactionCommit(boolean afterTransactionCommit) {
		this.afterTransactionCommit = afterTransactionCommit;
	}

	
}
