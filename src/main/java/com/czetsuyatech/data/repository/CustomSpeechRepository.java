package com.czetsuyatech.data.repository;

import com.czetsuyatech.data.entity.Speech;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
public interface CustomSpeechRepository {

	List<Speech> findByMetadata(String author, String keyword, String text, LocalDate from, LocalDate to);
}
