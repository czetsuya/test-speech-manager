package com.czetsuyatech.web.application;

import com.czetsuyatech.business.domain.BaseEntityDto;
import com.czetsuyatech.business.domain.SpeechDto;
import com.czetsuyatech.business.exception.ResourceNotFoundException;
import com.czetsuyatech.data.annotation.EntityCode;
import com.czetsuyatech.data.entity.BaseEntity;
import com.czetsuyatech.utils.DateUtils;
import com.czetsuyatech.utils.ReflectionUtils;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javax.transaction.NotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Base REST endpoint controller that contain common methods.
 *
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
public abstract class AbstractController<E extends BaseEntity, D extends BaseEntityDto, I extends Serializable> {

	public static final int DEFAULT_PAGE_SIZE = 10;

	protected Class<E> entityClass;
	protected Class<D> dtoClass;

	@Autowired
	protected @Qualifier("validator")
	Validator validator;

	public AbstractController() {

		Class clazz = getClass();
		entityClass = (Class<E>) ReflectionUtils.getParameterTypeClass(clazz, 0);
		dtoClass = (Class<D>) ReflectionUtils.getParameterTypeClass(clazz, 1);
	}

	/**
	 * Binds the validator and default date format. For example, if we passed a string {@linkplain java.util.Date} with the given format it is automatically parsed.
	 *
	 * @param binder data binder
	 * @see CustomDateEditor
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {

		binder.setValidator(validator);

		SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.SDF_STRING);
		binder.registerCustomEditor(LocalDate.class, new CustomDateEditor(dateFormat, true));
	}

	protected Pageable initPage(Integer page, Integer size) {

		if (size == null) {
			size = DEFAULT_PAGE_SIZE;
		}
		if (page == null) {
			page = 0;
		}

		return PageRequest.of(page, size);
	}

	protected ResourceNotFoundException createNewResourceNotFoundException(Serializable id) {

		String errorCode = "";
		try {
			errorCode = entityClass.getAnnotation(EntityCode.class).value();

		} catch (NullPointerException npe) {
		}

		return new ResourceNotFoundException(errorCode, entityClass.getSimpleName(), id);
	}

	public EntityModel<SpeechDto> findById(@PathVariable Long id) throws NotSupportedException {

		return null;
	}

	public CollectionModel<EntityModel<SpeechDto>> findAll(@RequestParam(required = false) Integer size
		, @RequestParam(required = false) Integer page) throws NotSupportedException {

		return null;
	}
}
