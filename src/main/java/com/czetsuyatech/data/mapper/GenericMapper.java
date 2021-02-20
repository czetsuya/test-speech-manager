package com.czetsuyatech.data.mapper;

import java.util.List;
import org.mapstruct.Mapping;

public interface GenericMapper<E, D> {

	D toDto(E source);

	@Mapping(target = "id", ignore = true)
	E toModel(D target);

	List<D> toDto(List<E> sourceList);

	List<E> toModel(List<D> targetList);
}