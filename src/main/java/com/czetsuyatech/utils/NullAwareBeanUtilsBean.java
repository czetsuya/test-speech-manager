package com.czetsuyatech.utils;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtilsBean;

/**
 * Custom {@link BeanUtilsBean} that does not copy null values.
 *
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
public class NullAwareBeanUtilsBean extends BeanUtilsBean {

	/**
	 * Copy source property to destination. Null value will not be copy. To nullify a field set it to an empty space.
	 */
	@Override
	public void copyProperty(Object dest, String name, Object value) throws IllegalAccessException, InvocationTargetException {

		if (value == null) {
			return;
		}

		if (value == "") {
			value = null;
		}

		super.copyProperty(dest, name, value);
	}
}