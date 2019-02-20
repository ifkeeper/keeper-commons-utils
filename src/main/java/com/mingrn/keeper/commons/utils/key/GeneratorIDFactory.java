package com.mingrn.keeper.commons.utils.key;

/**
 * 生成id,所有的id生成必须调用该类的下的方法
 *
 * @author MinGRn
 */
public class GeneratorIDFactory {
	private GeneratorIDFactory() {
	}

	public static String generatorId(Generator generator) {
		return generator.generateID();
	}

	/**
	 * 生成UserId
	 */
	public static String generatorUserId() {
		return generatorId(UserIdGenerator.getInstance());
	}

	/**
	 * 生成UUID
	 */
	public static String generatorUUID() {
		return generatorId(UUIDGenerator.getInstance());
	}

}
