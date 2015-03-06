/**
 * 
 */
package com.tenpay.sm.lang.extend;

import java.beans.PropertyDescriptor;
import java.io.Serializable;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 扩展，理念参考和类似osgi:extension
 * 但是不用定义扩展点，可以对任意bean扩展
 * @author li.hongtl
 *
 */
abstract public class Extension implements Serializable,
//BeanPostProcessor,
InstantiationAwareBeanPostProcessor,
InitializingBean,
ApplicationContextAware
{
	private static final Log LOG = LogFactory.getLog(Extension.class);
	private static final long serialVersionUID = -7929873978263279808L;
	
	private ApplicationContext applicationContext;
	/**
	 * 被扩展的bean
	 */
	private Object extensionPointBean;
	
	private String extensionPointBeanName;

	public Object postProcessAfterInitialization(Object bean, String beanName)
		throws BeansException {
//		System.out.println(beanName);
		if(LOG.isDebugEnabled()) {
			LOG.debug("调用扩展postProcessAfterInitialization方法, beanName: " + beanName
					+ " 需要被扩展的bean: " + extensionPointBean 
					+ " 被扩展的beanName: " + extensionPointBeanName);
		}
		if(extensionPointBean==bean || 
				(beanName!=null && beanName.equals(extensionPointBeanName))) {
			this.registExtensionToExtensionpPoint(bean);
		}
		return bean;
	}
	
	/* (non-Javadoc)
	* @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
	*/
	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		
//		for(String name : this.applicationContext.getBeanDefinitionNames()) {
//			System.out.print(name + ",");
//		}
//		System.out.println(beanName);
//		System.out.println(this.applicationContext.getBeanDefinitionCount());

//		if("payOrder".equals(beanName) || (bean!=null && bean.getClass().getSimpleName().equals("PayOrder"))) {
//			System.out.print("payOrder");
//			int n = 3 + 2;
//		}

		return bean;
	}
		
	/**
	 * Apply this BeanPostProcessor <i>before the target bean gets instantiated</i>.
	 * The returned bean object may be a proxy to use instead of the target bean,
	 * effectively suppressing default instantiation of the target bean.
	 * <p>If a non-null object is returned by this method, the bean creation process
	 * will be short-circuited. The only further processing applied is the
	 * {@link #postProcessAfterInitialization} callback from the configured
	 * {@link BeanPostProcessor BeanPostProcessors}.
	 * <p>This callback will only be applied to bean definitions with a bean class.
	 * In particular, it will not be applied to beans with a "factory-method".
	 * <p>Post-processors may implement the extended
	 * {@link SmartInstantiationAwareBeanPostProcessor} interface in order
	 * to predict the type of the bean object that they are going to return here.
	 * @param beanClass the class of the bean to be instantiated
	 * @param beanName the name of the bean
	 * @return the bean object to expose instead of a default instance of the target bean,
	 * or <code>null</code> to proceed with default instantiation
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see org.springframework.beans.factory.support.AbstractBeanDefinition#hasBeanClass
	 * @see org.springframework.beans.factory.support.AbstractBeanDefinition#getFactoryMethodName
	 */
	public Object postProcessBeforeInstantiation(Class beanClass, String beanName) throws BeansException {
		return null;
	}

	/**
	 * Perform operations after the bean has been instantiated, via a constructor or factory method,
	 * but before Spring property population (from explicit properties or autowiring) occurs.
	 * <p>This is the ideal callback for performing field injection on the given bean instance.
	 * See Spring's own {@link org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor}
	 * for a typical example.
	 * @param bean the bean instance created, with properties not having been set yet
	 * @param beanName the name of the bean
	 * @return <code>true</code> if properties should be set on the bean; <code>false</code>
	 * if property population should be skipped. Normal implementations should return <code>true</code>.
	 * Returning <code>false</code> will also prevent any subsequent InstantiationAwareBeanPostProcessor
	 * instances being invoked on this bean instance.
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
//		System.out.println(beanName);
//		if(extensionPointBean==bean || 
//				(beanName!=null && beanName.equals(extensionPointBeanName))) {
//			this.registExtensionToExtensionpPoint(bean);
//		}
		return true;
	}

	/**
	 * Post-process the given property values before the factory applies them
	 * to the given bean. Allows for checking whether all dependencies have been
	 * satisfied, for example based on a "Required" annotation on bean property setters.
	 * <p>Also allows for replacing the property values to apply, typically through
	 * creating a new MutablePropertyValues instance based on the original PropertyValues,
	 * adding or removing specific values.
	 * @param pvs the property values that the factory is about to apply (never <code>null</code>)
	 * @param pds the relevant property descriptors for the target bean (with ignored
	 * dependency types - which the factory handles specifically - already filtered out)
	 * @param bean the bean instance created, but whose properties have not yet been set
	 * @param beanName the name of the bean
	 * @return the actual property values to apply to to the given bean
	 * (can be the passed-in PropertyValues instance), or <code>null</code>
	 * to skip property population
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see org.springframework.beans.MutablePropertyValues
	 */
	public PropertyValues postProcessPropertyValues(
			PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName)
			throws BeansException {
		return pvs;
	}
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if(LOG.isDebugEnabled()) {
			LOG.debug("调用扩展afterPropertiesSet方法"
					+ " 需要被扩展的bean: " + extensionPointBean 
					+ " 被扩展的beanName: " + extensionPointBeanName);
		}
		registExtensionToExtensionpPoint(this.extensionPointBean);
	}

	abstract public void registExtensionToExtensionpPoint(Object extensionpPointBean);
	

	
	public Object getExtensionPointBean() {
		return extensionPointBean;
	}

	public void setExtensionPointBean(Object extensionPointBean) {
		this.extensionPointBean = extensionPointBean;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public String getExtensionPointBeanName() {
		return extensionPointBeanName;
	}

	public void setExtensionPointBeanName(String extensionPointBeanName) {
		this.extensionPointBeanName = extensionPointBeanName;
	}

	
	
	

}
