package com.mingrn.keeper.utils.object;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * Object工具类
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 */
public class ObjectUtils {

	private ObjectUtils() {
	}

	/**
	 * 判断对象为 NULL
	 *
	 * @param obj 对象
	 */
	public static boolean isNull(Object obj) {
		return obj == null;
	}

	/**
	 * 判断对象不为 NULL
	 *
	 * @param obj 对象
	 */
	public static boolean isNotNull(Object obj) {
		return !isNull(obj);
	}

	/**
	 * 判断对象不为空
	 *
	 * @param obj 对象
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		} else if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length() == 0;
		} else if (obj instanceof Collection) {
			return ((Collection) obj).isEmpty();
		} else if (obj instanceof Map) {
			return ((Map) obj).isEmpty();
		} else if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}
		return false;
	}

	/**
	 * 判断对象不为空
	 *
	 * @param obj 对象
	 */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}
}