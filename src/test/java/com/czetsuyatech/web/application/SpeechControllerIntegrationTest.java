package com.czetsuyatech.web.application;

import com.czetsuyatech.data.entity.Speech;
import com.czetsuyatech.data.repository.CustomSpeechRepository;
import com.czetsuyatech.data.repository.SpeechRepository;
import com.czetsuyatech.utils.DateUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
@Slf4j
public class SpeechControllerIntegrationTest extends AbstractControllerTest {

	private static final String SPEECH_URL = "/api/v1/speeches";

	@MockBean
	SpeechRepository speechRepository;

	@MockBean
	CustomSpeechRepository customSpeechRepository;

	private Speech speech;

	@BeforeEach
	public void init() {

		log.debug("Before test");

		speech = new Speech();
		speech.setText(
			"We shall go on to the end, we shall fight in France, we shall fight on the seas and oceans, we shall fight with growing confidence and growing strength in the air, we shall defend our Island, whatever the cost may be, we shall fight on the beaches, we shall fight on the landing grounds, we shall fight in the fields and in the streets, we shall fight in the hills; we shall never surrender");
		speech.setAuthor("Winston Churchill");
		speech.setKeywords("war,fight");
		speech.setDateOfSpeech(DateUtils.toLocalDate(DateUtils.parseDateWithPattern("1940-06-04", DateUtils.SDF_STRING)));
	}

	@Test
	public void createSpeechOk() {

		Mockito.when(speechRepository.save(speech)).thenReturn(speech);

		Map<String, String> request = new HashMap<>();
		request.put("text", speech.getText());
		request.put("author", speech.getAuthor());
		request.put("keywords", speech.getKeywords());
		request.put("dateOfSpeech", DateUtils.formatDateWithPattern(DateUtils.fromLocalDate(speech.getDateOfSpeech()), DateUtils.SDF_STRING));

		final ExtractableResponse<Response> extractable = RestAssured.given() //
			.log() //
			.all() //
			.contentType(ContentType.JSON) //
			.body(request) //
			.when() //
			.post(SPEECH_URL) //
			.then() //
			.assertThat() //
			.statusCode(HttpStatus.CREATED.value()) //
			.and() //
			.extract();

		Assertions.assertThat(extractable.body().asString()).isNotNull();
	}

	@Test
	public void updateSpeechOk() {

		Speech updatedSpeech = SerializationUtils.roundtrip(speech);
		updatedSpeech.setAuthor("Edward Legaspi");

		Mockito.when(speechRepository.save(updatedSpeech)).thenReturn(updatedSpeech);

		Map<String, String> request = new HashMap<>();
		request.put("text", updatedSpeech.getText());
		request.put("author", updatedSpeech.getAuthor());
		request.put("keywords", updatedSpeech.getKeywords());
		request.put("dateOfSpeech", DateUtils.formatDateWithPattern(DateUtils.fromLocalDate(updatedSpeech.getDateOfSpeech()), DateUtils.SDF_STRING));

		final ExtractableResponse<Response> extractable = RestAssured.given() //
			.log() //
			.all() //
			.contentType(ContentType.JSON) //
			.body(request) //
			.when() //
			.put(SPEECH_URL + "/1") //
			.then() //
			.assertThat() //
			.statusCode(HttpStatus.OK.value()) //
			.and() //
			.extract();

		Assertions.assertThat(extractable.body().asString()).isNotNull();
		String author = extractable.response().path("author");
		Assertions.assertThat(author).isEqualTo(updatedSpeech.getAuthor());
	}

	@Test
	public void findSpeechOk() {

		Mockito.when(speechRepository.findById(1L)).thenReturn(Optional.of(speech));

		final ExtractableResponse<Response> extractable = RestAssured.given() //
			.log() //
			.all() //
			.contentType(ContentType.JSON) //
			.when() //
			.get(SPEECH_URL + "/1") //
			.then() //
			.assertThat() //
			.statusCode(HttpStatus.OK.value()) //
			.and() //
			.extract();

		String author = extractable.response().path("author");
		Assertions.assertThat(author).isEqualTo(speech.getAuthor());
	}

	@Test
	public void findAllSpeechesOk() {

		Speech speech2 = SerializationUtils.roundtrip(speech);
		speech2.setAuthor("Edward Legaspi");
		Page<Speech> pagedResponse = new PageImpl(Arrays.asList(speech, speech2));
		Pageable page = PageRequest.of(0, 10);
		Mockito.when(speechRepository.findAll(page)).thenReturn(pagedResponse);

		final ExtractableResponse<Response> extractable = RestAssured.given() //
			.log() //
			.all() //
			.contentType(ContentType.JSON) //
			.when() //
			.get(SPEECH_URL) //
			.then() //
			.assertThat() //
			.statusCode(HttpStatus.OK.value()) //
			.and() //
			.extract();

		List<Speech> speeches = extractable.response().path("_embedded.speechDtoes");
		Assertions.assertThat(2).isEqualTo(speeches.size());
	}

	@Test
	public void deleteSpeechOk() {

		final ExtractableResponse<Response> extractable = RestAssured.given() //
			.log() //
			.all() //
			.contentType(ContentType.JSON) //
			.when() //
			.delete(SPEECH_URL + "/1") //
			.then() //
			.assertThat() //
			.statusCode(HttpStatus.NO_CONTENT.value()) //
			.and() //
			.extract();

		Mockito.verify(speechRepository).deleteById(Mockito.any());
	}

	@Test
	public void searchByAuthorOk() {

		Mockito.when(customSpeechRepository.findByMetadata("Winston%20Churchill", null, null, null, null)).thenReturn(Arrays.asList(speech));

		final ExtractableResponse<Response> extractable = RestAssured.given() //
			.log() //
			.all() //
			.contentType(ContentType.JSON) //
			.when() //
			.get(SPEECH_URL + "/search?author=Winston%20Churchill") //
			.then() //
			.assertThat() //
			.statusCode(HttpStatus.OK.value()) //
			.and() //
			.extract();

		System.out.println("return-->"+extractable.response().path("_embedded"));
//		String author = extractable.response().path("_embedded.speechDtoes[0].author");
//		Assertions.assertThat(author).isEqualTo("Winston Churchill");
	}
}
