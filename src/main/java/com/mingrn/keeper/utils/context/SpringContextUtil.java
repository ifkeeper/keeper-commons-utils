package com.mingrn.keeper.utils.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	public SpringContextUtil() {
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		if (applicationContext == null) {
			applicationContext = context;
		}

	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取bean
	 *
	 * @param name bean 名称
	 * @return T
	 */
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	/**
	 * 获取bean
	 *
	 * @param clazz bean 类型
	 * @return T
	 */
	public static <T> T getBean(Class<T> clazz) {
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
		return getApplicationContext().getBean(name, clazz);
	}
}