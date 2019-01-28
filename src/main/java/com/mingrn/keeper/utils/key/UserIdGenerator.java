package com.mingrn.keeper.utils.key;

import java.util.UUID;

/**
 * 用户id生成器
 *
 * @author MinGRn
 */
class UserIdGenerator implements Generator {

	private static Generator generator = new UserIdGenerator();

	private UserIdGenerator() {
	}

	/**
	 * 获取实例
	 *
	 * @see GeneratorIDFactory
	 */
	protected static Generator getInstance() {
		return generator;
	}

	@Override
	public String generateID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
