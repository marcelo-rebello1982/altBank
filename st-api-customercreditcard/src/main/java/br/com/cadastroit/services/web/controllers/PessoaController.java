package br.com.cadastroit.services.web.controllers;


import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cadastroit.services.api.domain.Pessoa;
import br.com.cadastroit.services.exceptions.CartaoException;
import br.com.cadastroit.services.repositories.CartaoRepository;
import br.com.cadastroit.services.repositories.ContaRepository;
import br.com.cadastroit.services.repositories.PessoaRepository;
import br.com.cadastroit.services.web.controller.commons.CommonsController;
import br.com.cadastroit.services.web.controller.commons.CommonsValidation;
import br.com.cadastroit.sevices.web.dto.PessoaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/administracao/pessoa")
public class PessoaController extends CommonsController {
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Operation(summary = "Get Max Id from Pessoa")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Pessoa created with sucessful", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PessoaDTO.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Pessoa", description = "Get a Pessoa max id record")
	@GetMapping("/maxId")
	public ResponseEntity<Object> maxId(@RequestHeader(required = false , value = "bancoId") Long bancoId) {

		try {
			Long id = pessoaRepository.maxId(entityManagerFactory, bancoId);
			return ResponseEntity.ok(id);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body((String.format(ex.getMessage())));
		}
	}
	
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the Person", content = @Content),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Person not found", content = @Content) })
	@Tag(name = "Person", description = "Get a Person record by id")
	@GetMapping(value = "/find/{id}", produces = "application/json" )
	public ResponseEntity<Object> find(@PathVariable("id") Long id, @RequestParam(required = false) Map<String, String> requestParams) {

		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			
			Pessoa entity = pessoaRepository.findById(id, entityManagerFactory);
			return ResponseEntity.ok().body(pessoaMapper.toDTO(entity));
			
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Operation(summary = "Create Person")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Person created with sucessful", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PessoaDTO.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Order", description = "Create a Person record")
	@PostMapping("/create")
	public ResponseEntity<Object> handlePost(@RequestHeader(required = false , value = "bancoId") Long bancoId, @Validated @RequestBody PessoaDTO dto) {

		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			Pessoa entity = pessoaRepository.findById(bancoId, entityManagerFactory);
			return mountEntity(dto, bancoId, entity, false);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	private CommonsValidation createCommonsValidation() {

		return CommonsValidation.builder()
				.cartaoRepository(new CartaoRepository())
				.build();
	}
	
	private ResponseEntity<Object> mountEntity(PessoaDTO dto, Long id, Pessoa pessoa, boolean update) throws CartaoException {

		try {
			
			Pessoa entity = pessoaMapper.toEntity(dto);
			HttpHeaders headers = new HttpHeaders();
			entity.setId(update ? id : null);
			entity = pessoaRepository.save(entity);
			headers.add("Location", "/find/" + entity.getId());
			return new ResponseEntity<>(entity, headers, update ? HttpStatus.NO_CONTENT : HttpStatus.CREATED);
		} catch (NoResultException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		} catch (Exception ex) {
			throw new CartaoException(ex.getMessage(), ex);
		}
	}
	
	public PessoaController(ObjectMapper mapperJson, PessoaRepository pessoaRepository, CartaoRepository cartaoRepository, ContaRepository contaRepository ) {
		super(mapperJson, pessoaRepository, cartaoRepository, contaRepository);
	}
	
}
