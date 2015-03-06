package com.tenpay.sm.web.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.handler.AbstractHandlerMapping;
/**
 * 
 */

import com.tenpay.sm.web.controller.DefaultController;

/**
 * 没有任何Handler可以匹配的时候，最后用的HandlerMapping
 * 用DefaultController作为handler
 * 优先级必须最低，order为Integer.MAX_VALUE
 * @author li.hongtl
 *
 */
public class DefaultHandlerMapping extends AbstractHandlerMapping 
//implements BeanFactoryPostProcessor 
{
	//BeanFactoryPostProcessor bfpp;
	private DefaultController defaultController;
	public DefaultHandlerMapping() {
		this.setOrder(Integer.MAX_VALUE);
	}
	
	@Override
	protected Object getHandlerInternal(HttpServletRequest arg0) throws Exception {
		return defaultController;
	}

	public void setDefaultController(DefaultController defaultController) {
		this.defaultController = defaultController;
	}

//	public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) throws BeansException {
//		// TODO Auto-generated method stub
//		WebApplicationContext wpc = this.getWebApplicationContext();
//		ApplicationContext ac = this.getApplicationContext();
//		try {
//			//bf.configureBean(arg0, arg1)(arg0, arg1)
//			//bf.autowire(arg0, arg1, MyVariableResolverImpl)
//			//this.getClass().getClasses()
//			//this.getClass().
//			//bf.
//
//			//bf.configureBean(bean, "/sm/pojotradegoodsquery2");
//			//bf.a
//			int bs1 = bf.getSingletonCount();
//			
////			Object bean = bf.createBean(Class
////					.forName("com.smpoc.demo.sm.module.PojoTradeGoodsQuery"),
////					AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true);
////			bf.registerSingleton("/sm/pojotradegoodsquery2", bean);
//			
//			DefaultListableBeanFactory  dlbf = (DefaultListableBeanFactory )bf;
//			GenericBeanDefinition bd = new GenericBeanDefinition();
//			bd.setBeanClass(Class.forName("com.smpoc.demo.sm.module.PojoTradeGoodsQuery"));
//			bd.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_NAME);
//			dlbf.registerBeanDefinition("/sm/pojotradegoodsquery2", bd);
//			
//			Object bean2 = bf.getBean("/sm/pojotradegoodsquery2");
//			int bs2 = bf.getSingletonCount();
//			
////			Locale.setDefault(Locale.GERMAN);
////			GenericApplicationContext context = new GenericApplicationContext();
////			ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(context);
////			scanner.scan("blog"); // the parameter is 'basePackage'
////			context.refresh();
////			GreetingService greetingService = (GreetingService) context.getBean("greetingServiceImpl");
////			String message = greetingService.greet("Standalone Beans");
////			System.out.println(message);
//
////			ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(context, useDefaultFilters);
////			scanner.addExcludeFilter(new AssignableTypeFilter(JdbcMessageRepository.class));
////			scanner.addIncludeFilter(new AnnotationTypeFilter(Component.class));
////			scanner.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile("blog\\.Stub.*")));
////			scanner.scan("blog");
//			
////			<context:component-scan base-package="blog"
////                name-generator="blog.MyBeanNameGenerator"
////                scope-resolver="blog.MyScopeMetadataResolver"/>
////
////
////			<context:component-scan base-package="blog" use-default-filters="false">
////			<context:include-filter type="annotation" expression="org.springframework.stereotype.Component"/>
////			<context:include-filter type="regex" expression="blog\.Stub.*"/>
////			<context:exclude-filter type="assignable" expression="blog.JdbcMessageRepository"/>
////			</context:component-scan>
//			
//			//不过，你可以参考一下Spring内部提供的两个类，它的做法应该是标准的。
//			//一个是PathMatchingResourcePatternResolver，用于找到一个package下所有的Resource，
//			//一个是ClassUtils，用于将资源转成类。它的实现比较全面。
//
////		    <context:property-placeholder location="classpath:blog/jdbc.properties"/>
////		    
////		    <bean class="org.springframework.jdbc.datasource.DriverManagerDataSource">
////		        <property name="driverClassName" value="${jdbc.driver}"/>
////		        <property name="url" value="${jdbc.url}"/>
////		        <property name="username" value="${jdbc.username}"/>
////		        <property name="password" value="${jdbc.password}"/>
////		    </bean>
//			int n = 1+2;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}		
//
//		return;
//	}
	
}
