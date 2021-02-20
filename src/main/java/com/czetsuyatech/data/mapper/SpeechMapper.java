package com.czetsuyatech.data.mapper;

import com.czetsuyatech.business.domain.SpeechDto;
import com.czetsuyatech.data.entity.Speech;
import org.mapstruct.Mapper;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
@Mapper
public abstract class SpeechMapper implements GenericMapper<Speech, SpeechDto> {

}
