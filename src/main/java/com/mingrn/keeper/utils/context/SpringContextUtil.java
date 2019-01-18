package com.mingrn.keeper.utils.context;

import org.springframework.beans.BeansException;
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

	public static ApplicationContext getApplicationContext() {
		checkApplicationContext();
		return applicationContext;
	}

	/**
	 * 获取bean
	 *
	 * @param name bean 名称
	 * @return T
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
	 * 检查 applicationContext
	 */
	private static void checkApplicationContext() {
		if (applicationContext == null) {
			throw new IllegalStateException("ApplicationContext not injected please define the SpringContextUtil use annotations or spring.xml");
		}
	}
}