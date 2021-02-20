package com.czetsuyatech.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
public class ReflectionUtils {

	private ReflectionUtils() {

	}

	@SuppressWarnings("rawtypes")
	public static Object getParameterTypeClass(Class clazz, int parameterIndex) {
		while (!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
			clazz = clazz.getSuperclass();
		}

		Object o = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[parameterIndex];

		if (o instanceof TypeVariable) {
			return ((TypeVariable) o).getBounds()[parameterIndex];

		} else {
			return o;
		}
	}
}
