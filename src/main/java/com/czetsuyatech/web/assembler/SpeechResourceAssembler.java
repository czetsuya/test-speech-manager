package com.czetsuyatech.web.assembler;

import com.czetsuyatech.business.domain.SpeechDto;
import com.czetsuyatech.web.application.SpeechController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
@Component
public class SpeechResourceAssembler extends AbstractResourceAssembler<SpeechDto> implements RepresentationModelAssembler<SpeechDto, EntityModel<SpeechDto>> {

	public SpeechResourceAssembler() {
		super(SpeechController.class);
	}
}

