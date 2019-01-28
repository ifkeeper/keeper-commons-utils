package com.mingrn.keeper.utils.key;

import java.util.UUID;

/**
 * 生成32位UUID
 *
 * @author MinGR
 */
class UUIDGenerator implements Generator {

	private static Generator generator = new UUIDGenerator();

	private UUIDGenerator() {
	}

	/**
	 * 获取实例
	 *
	 * @see GeneratorIDFactory
	 */
	protected static Generator getInstance() {
		return generator;
	}

	/**
	 * 随机生成UUID
	 *
	 * @return String
	 */
	@Override
	public String generateID() {
		return UUID.randomUUID().toString();
	}
}
