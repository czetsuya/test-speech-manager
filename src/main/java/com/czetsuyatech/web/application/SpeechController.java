package com.czetsuyatech.web.application;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.czetsuyatech.business.domain.ShareSpeechDto;
import com.czetsuyatech.business.domain.SpeechDto;
import com.czetsuyatech.business.service.SpeechService;
import com.czetsuyatech.data.entity.Speech;
import com.czetsuyatech.data.mapper.GenericMapper;
import com.czetsuyatech.data.mapper.GenericMapperService;
import com.czetsuyatech.data.repository.CustomSpeechRepository;
import com.czetsuyatech.data.repository.SpeechRepository;
import com.czetsuyatech.utils.NullAwareBeanUtilsBean;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for managing {@link com.czetsuyatech.data.entity.Speech}.
 *
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @version 0.0.1
 * @since 0.0.1
 */
@RestController
@RequestMapping(path = "/speeches", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Slf4j
public class SpeechController extends AbstractController<Speech, SpeechDto, Long> {

	private final GenericMapperService<Speech, SpeechDto> genericMapperService;

	private final GenericMapper<Speech, SpeechDto> genericMapper;

	private final RepresentationModelAssembler<SpeechDto, EntityModel<SpeechDto>> modelAssembler;

	private final SpeechRepository repository;

	private final CustomSpeechRepository customSpeechRepository;

	private final SpeechService speechService;

	public SpeechController(
		GenericMapperService<Speech, SpeechDto> genericMapperService,
		GenericMapper<Speech, SpeechDto> genericMapper,
		RepresentationModelAssembler<SpeechDto, EntityModel<SpeechDto>> modelAssembler,
		SpeechRepository repository,
		CustomSpeechRepository customSpeechRepository,
		SpeechService speechService) {
		this.genericMapperService = genericMapperService;
		this.genericMapper = genericMapper;
		this.modelAssembler = modelAssembler;
		this.repository = repository;
		this.customSpeechRepository = customSpeechRepository;
		this.speechService = speechService;
	}

	/**
	 * Creates a speech object.
	 *
	 * @param dto speech info
	 * @return the created speech
	 */
	@PostMapping
	public ResponseEntity<EntityModel<SpeechDto>> create(@RequestBody @NotNull @Valid SpeechDto dto) {

		Speech entity = genericMapper.toModel(dto);

		final EntityModel<SpeechDto> resource =
			modelAssembler.toModel(genericMapper.toDto(repository.save(entity)));
		return ResponseEntity.created(
			linkTo(SpeechController.class).slash(entity.getId()).withSelfRel().toUri())
			.body(resource);
	}

	/**
	 * Updates a speech object given its id.
	 *
	 * @param newDto the new speech object
	 * @param id     id of speech to be updated
	 * @return the updated speech
	 */
	@PutMapping(path = "/{id}")
	public ResponseEntity<SpeechDto> update(
		@RequestBody SpeechDto newDto, @PathVariable @NotNull Long id) {

		Speech newEntity = genericMapper.toModel(newDto);

		Speech updatedEntity =
			repository
				.findById(id)
				.map(
					entity -> {
						// entity = SerializationUtils.roundtrip(newEntity);
						try {
							BeanUtilsBean bean = new NullAwareBeanUtilsBean();
							bean.copyProperties(entity, newEntity);

						} catch (IllegalAccessException | InvocationTargetException e) {
							log.warn("Update failed: {}", e.getMessage());
						}
						return repository.save(entity);
					})
				.orElseGet(
					() -> {
						newEntity.setId(id);
						return repository.save(newEntity);
					});

		return ResponseEntity.ok().body(genericMapper.toDto(updatedEntity));
	}

	/**
	 * Retrieves a speech object given its id.
	 *
	 * @param id id of the speech
	 * @return the speech object of the given id
	 */
	@Override
	@GetMapping(path = "/{id}")
	public EntityModel<SpeechDto> findById(@PathVariable Long id) {

		Speech entity =
			repository.findById(id).orElseThrow(() -> createNewResourceNotFoundException(id));

		return modelAssembler.toModel(genericMapper.toDto(entity));
	}

	/**
	 * Retrieves all the speech objects in the database.
	 *
	 * @param size number of speech to retrieve
	 * @param page page number to retrieve
	 * @return a collection of the retrieve speech objects
	 */
	@Override
	@GetMapping
	public CollectionModel<EntityModel<SpeechDto>> findAll(@RequestParam(required = false) Integer size, @RequestParam(required = false) Integer page) {

		Pageable pageable = initPage(page, size);

		List<EntityModel<SpeechDto>> entities =
			repository.findAll(pageable).stream()
				.map(e -> modelAssembler.toModel(genericMapper.toDto(e)))
				.collect(Collectors.toList());

		return CollectionModel.of(
			entities, linkTo(methodOn(SpeechController.class).findAll(size, page)).withSelfRel());
	}

	/**
	 * Removes a speech object given its id
	 *
	 * @param id id of the speech to be remove
	 * @return normally no content status
	 */
	@DeleteMapping(path = "/{id}")
	public ResponseEntity delete(@PathVariable @NotNull Long id) {

		repository.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	/**
	 * Retrieves a list of speech object given a list of criterias.
	 *
	 * @param author   speech author
	 * @param fromDate must be after or equal this date
	 * @param toDate   must be before or equal this date
	 * @param keyword  match keyword of speech
	 * @param text     check if speech body contains this fragment
	 * @return collection of speech that match the criterias
	 */
	@GetMapping(path = "/search")
	public CollectionModel<EntityModel<SpeechDto>> search(
		@RequestParam(required = false) String author,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate fromDate,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate toDate,
		@RequestParam(required = false) String keyword,
		@RequestParam(required = false) String text) {

		List<EntityModel<SpeechDto>> entities =
			customSpeechRepository.findByMetadata(author, keyword, text, fromDate, toDate).stream()
				.map(e -> modelAssembler.toModel(genericMapper.toDto(e)))
				.collect(Collectors.toList());

		return CollectionModel.of(
			entities,
			linkTo(methodOn(SpeechController.class).search(author, fromDate, toDate, keyword, text))
				.withSelfRel());
	}

	/**
	 * Share a speech given its id via email
	 *
	 * @param id  id of speech
	 * @param dto metadata of info to share the speech, for now it only contains the email of the recipient
	 */
	@PostMapping(path = "/{id}/share")
	public void shareSpeech(
		@PathVariable @NotNull Long id, @RequestBody @NotNull @Valid ShareSpeechDto dto) {

		speechService.shareSpeech(id, dto.getEmailTo());
	}
}
