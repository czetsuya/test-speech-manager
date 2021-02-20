package com.czetsuyatech.business.domain;

import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class SpeechDto extends BaseEntityDto {

	private String text;
	private String author;
	private String keywords;
	private LocalDate dateOfSpeech;
}
