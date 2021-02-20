package com.czetsuyatech.business.service;

import com.czetsuyatech.data.entity.Speech;
import com.czetsuyatech.data.repository.SpeechRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
@Service
public class SpeechService {

	private final SpeechRepository speechRepository;

	private final EmailService emailService;

	public SpeechService(SpeechRepository speechRepository, EmailService emailService) {
		this.speechRepository = speechRepository;
		this.emailService = emailService;
	}

	public void shareSpeech(Long speechId, String emailTo) {

		Speech speech =
			Optional.ofNullable(speechRepository.findById(speechId))
				.orElseThrow(RuntimeException::new)
				.get();

		String body = String.format("Author: %s\r\n\r\nSpeech\r\n%s", speech.getAuthor(), speech.getText());
		emailService.sendMessage("oreply@czetsuyatech.com", emailTo, "Sharing speech with id " + speechId, body);
	}
}
