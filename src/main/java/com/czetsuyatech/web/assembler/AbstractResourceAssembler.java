package com.czetsuyatech.web.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.czetsuyatech.business.domain.BaseEntityDto;
import com.czetsuyatech.web.application.AbstractController;
import javax.transaction.NotSupportedException;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
@NoArgsConstructor
@SuppressWarnings("rawtypes")
public abstract class AbstractResourceAssembler<D extends BaseEntityDto> implements RepresentationModelAssembler<D, EntityModel<D>> {

	private Class<? extends AbstractController> controllerClass;

	public AbstractResourceAssembler(Class<? extends AbstractController> controllerClass) {

		this.controllerClass = controllerClass;
	}

	@Override
	public EntityModel<D> toModel(D entity) {

		EntityModel<D> result = new EntityModel<>(entity);
		try {
			result = result.add(linkTo(methodOn(controllerClass).findById(entity.getId())).withSelfRel());
			result = result.add(linkTo(methodOn(controllerClass).findAll(AbstractController.DEFAULT_PAGE_SIZE, 0)).withRel("all"));

		} catch (NotSupportedException e) {

		}

		return result;
	}
}
