package com.czetsuyatech.data.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
@Service
@ApplicationScope
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GenericMapperService<S, T> {

	private final Map<String, GenericMapper<S, T>> mapperInfos = new HashMap<>();
	private final ApplicationContext appContext;

	@Autowired
	public GenericMapperService(ApplicationContext appContent) {

		this.appContext = appContent;
	}

	/**
	 * Initialize the mappers by getting all the class that implements the {@linkplain GenericMapper}.
	 */
	@SuppressWarnings("unchecked")
	@PostConstruct
	private void initMappers() {

		Collection<GenericMapper> genericMappers = appContext.getBeansOfType(GenericMapper.class).values();

		for (GenericMapper mapper : genericMappers) {

			String sourceType = getMapperSourceType(mapper);
			String targetType = getMapperTargetType(mapper);

			mapperInfos.put(sourceType + "_" + targetType, mapper);
			mapperInfos.put(targetType + "_" + sourceType, mapper);
		}
	}

	/**
	 * Gets the source type of the mapper.
	 *
	 * @param mapper the generic mapper
	 * @return mapper type
	 */
	private String getMapperSourceType(GenericMapper mapper) {

		Class<?>[] mapperTypeInfos = GenericTypeResolver.resolveTypeArguments(mapper.getClass(), GenericMapper.class);

		return mapperTypeInfos[0].getSimpleName();
	}

	/**
	 * Gets the target type of the mapper.
	 *
	 * @param mapper the generic mapper
	 * @return mapper type
	 */
	private String getMapperTargetType(GenericMapper mapper) {

		Class<?>[] mapperTypeInfos = GenericTypeResolver.resolveTypeArguments(mapper.getClass(), GenericMapper.class);

		return mapperTypeInfos[1].getSimpleName();
	}

	/**
	 * Retrieves the mapper given a source and target type.
	 *
	 * @param sourceType the source object
	 * @param targetType the target object
	 * @return the mapper for the source and target object
	 */
	@Cacheable(value = "genericMappers")
	public GenericMapper<S, T> getMapper(Class<S> sourceType, Class<T> targetType) {

		String mapperKey = sourceType.getSimpleName() + "_" + targetType.getSimpleName();

		return mapperInfos.get(mapperKey);
	}
}
