package com.czetsuyatech.business.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends InvalidConfigurationPropertyValueException {

	private static final long serialVersionUID = -3959094123571588527L;
	private final String errorCode;
	private static final String REASON = "not found";

	public ResourceNotFoundException() {
		super(null, null, null);
		errorCode = "";
	}

	public ResourceNotFoundException(String simpleName, Serializable id) {

		this(null, simpleName, id);
	}

	public ResourceNotFoundException(Class<?> clazz, String code) {

		this(null, clazz.getSimpleName(), code);
	}

	public ResourceNotFoundException(String errorCode, String simpleName, Serializable id) {

		super(simpleName, id, REASON);
		this.errorCode = errorCode;
	}

	@Override
	public String getLocalizedMessage() {

		return (StringUtils.isBlank(errorCode) ? "" : errorCode + ": ") + super.getLocalizedMessage();
	}
}
