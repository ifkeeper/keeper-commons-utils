package com.mingrn.keeper.commons.utils.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring Content
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019/1/18 16:15
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	private SpringContextUtil() {
	}

	/**
	 * 实现ApplicationContextAware接口的回调方法，设置上下文环境
	 *
	 * @param applicationContext {@link ApplicationContext}
	 * @throws org.springframework.beans.BeansException
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextUtil.applicationContext = applicationContext;
	}

	/**
	 * 获取 {@link ApplicationContext}
	 */
	public static ApplicationContext getApplicationContext() {
		checkApplicationContext();
		return applicationContext;
	}

	/**
	 * 获取 bean
	 *
	 * @param name bean 名称
	 * @return {@link Object}
	 */
	public static Object getBean(String name) {
		checkApplicationContext();
		return getApplicationContext().getBean(name);
	}

	/**
	 * 获取bean
	 *
	 * @param clazz bean 类型
	 * @return T
	 */
	public static <T> T getBean(Class<T> clazz) {
		checkApplicationContext();
		return getApplicationContext().getBean(clazz);
	}

	/**
	 * 获取bean
	 *
	 * @param name bean 名称
	 * @param clazz bean 类型
	 * @return T
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		checkApplicationContext();
		return getApplicationContext().getBean(name, clazz);
	}

	/**
	 * BeanFactory是否包含指定名称bean定义
	 *
	 * @param name bean 名称
	 * @return {@link Boolean}
	 */
	public static boolean containsBean(String name) {
		checkApplicationContext();
		return applicationContext.containsBean(name);
	}

	/**
	 * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
	 * 如果与给定名字相应的bean定义没有被找到，将会抛出 {@link NoSuchBeanDefinitionException} 异常
	 *
	 * @param name bean 名称
	 * @return {@link Boolean}
	 * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
	 */
	public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		checkApplicationContext();
		return applicationContext.isSingleton(name);
	}

	/**
	 * 获取 bean 类型
	 *
	 * @param name bean 名称
	 * @return Class 注册对象的类型
	 * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
	 */
	public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		checkApplicationContext();
		return applicationContext.getType(name);
	}

	/**
	 * 获取指定 bean名字在bean定义中的别名
	 *
	 * @param name bean 名称
	 * @return {@link String...}
	 * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
	 */
	public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
		return applicationContext.getAliases(name);
	}

	/**
	 * 检查 applicationContext
	 */
	private static void checkApplicationContext() {
		if (applicationContext == null) {
			throw new IllegalStateException("ApplicationContext not injected please define the SpringContextUtil use annotations: `@Component` or spring.xml");
		}
	}
}