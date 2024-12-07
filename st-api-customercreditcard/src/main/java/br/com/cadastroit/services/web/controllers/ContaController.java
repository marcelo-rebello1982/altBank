package br.com.cadastroit.services.web.controllers;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cadastroit.services.api.domain.Cartao;
import br.com.cadastroit.services.api.domain.Conta;
import br.com.cadastroit.services.api.domain.HealthCheck;
import br.com.cadastroit.services.api.domain.Pessoa;
import br.com.cadastroit.services.api.enums.CartaoStatus;
import br.com.cadastroit.services.exceptions.CartaoException;
import br.com.cadastroit.services.repositories.CartaoRepository;
import br.com.cadastroit.services.repositories.ContaRepository;
import br.com.cadastroit.services.repositories.PessoaRepository;
import br.com.cadastroit.services.web.controller.commons.CommonsController;
import br.com.cadastroit.services.web.controller.commons.CommonsValidation;
import br.com.cadastroit.sevices.web.dto.CartaoDTO;
import br.com.cadastroit.sevices.web.dto.ContaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/administracao/conta")
public class ContaController extends CommonsController {
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Operation(summary = "Get status from API - ")
	@GetMapping("/status")
	public ResponseEntity<Object> status(
			@RequestParam(name = "status", required = false, defaultValue = "UP") String status) {
		try {
			return new ResponseEntity<>(
					HealthCheck.builder().status(status).maxId(cartaoRepository.maxId(entityManagerFactory)).build(),
					HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@Operation(summary = "Get Max Id from Account")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Account created with sucessful", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ContaDTO.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Cartao", description = "Get a Account max id record")
	@GetMapping("/maxId")
	public ResponseEntity<Object> maxId(@RequestHeader("pessoaId") Long pessoaId) {

		try {
			Long id = contaRepository.maxId(entityManagerFactory, pessoaId);
			return ResponseEntity.ok(id);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body((String.format(ex.getMessage())));
		}
	}
	
	@Operation(summary = "Get a Account by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the Account", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Conta.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Account not found", content = @Content) })
	@Tag(name = "Account", description = "Get a Account record by id")
	@GetMapping(value = "/find/{id}", produces = "application/json" )
	public ResponseEntity<Object> find(@PathVariable("id") Long id) {

		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			
			Conta entity = contaRepository.findById(id, entityManagerFactory);
			
			if (Boolean.valueOf(entity.getInativada()))
				return ResponseEntity.ok().body(contaMapper.toDisabled(entity));
			else return ResponseEntity.ok().body(contaMapper.toDTO(entity));
			
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Operation(summary = "Get a Account by Id or Pessoa id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the Account", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Conta.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Account not found", content = @Content) })
	@Tag(name = "Account", description = "Get a Account record by id")
	@GetMapping(value = "/find/{id}/{pessoaId}", produces = "application/json" )
	public ResponseEntity<Object> find(@PathVariable(required = false, value = "id") Long id, @RequestHeader(required = false, value = "pessoaId") Long pessoaId, @RequestParam(required = false) Map<String, String> requestParams ) {

		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			
			Conta entity = contaRepository.findById(id, pessoaId, entityManagerFactory);
			
			return ResponseEntity.ok().body(contaMapper.toDTO(entity));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Operation(summary = "Create Account")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Account created with sucessful", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ContaDTO.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Account", description = "Create a new Account record")
	@PostMapping(value = "/create", produces = "application/json" )
	public ResponseEntity<Object> handlePost(@RequestHeader(required = true , value = "pessoaId") Long pessoaId, @Validated @RequestBody ContaDTO dto) {

		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			Pessoa entity = pessoaRepository.findById(pessoaId, entityManagerFactory);
			return mountEntity(dto, pessoaId, entity, false);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Tag(name = "Account", description = "Disable/Enable a Account record")
	@PatchMapping(value = "/disableAccount/{id}",  produces = "application/json"  )
	public ResponseEntity<Object> handleDisableAccount(@RequestHeader(required =  false , value = "pessoaId") Long pessoaId, @PathVariable("id") Long id, @Validated @RequestBody ContaDTO dto) {

		CommonsValidation commonsValidation = this.createCommonsValidation();
		
		// {
		//    "id": 2,
		//    "inativada": true
		// }

		try {
			return this.mountEntity(dto, id, null, true);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Tag(name = "Account", description = "Updating a Account record")
	@PutMapping("/update/{id}")
	public ResponseEntity<Object> handleUpdate(@RequestHeader(required =  false , value = "pessoaId") Long pessoaId, @PathVariable("id") Long id, @Validated @RequestBody ContaDTO dto) {

		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			Pessoa entity = pessoaRepository.findById(pessoaId, entityManagerFactory);
			return this.mountEntity(dto, id, entity, true);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Operation(summary = "Delete Account By id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "No Content", description = "Update Credit Card", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Order", description = "Deleting a Credit Card record")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> delete(@PathVariable("id") Long id) throws CartaoException {

		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			contaRepository.delete(contaRepository.findById(id, entityManagerFactory));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Operation(summary = "Search all Account Card")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the List of Account", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cartao.class))) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Account", description = "Get all records using pagination mechanism")
	@GetMapping(value = "/all/{page}/{length}" , produces = "application/json" )
	public ResponseEntity<Object> all(@RequestHeader(required = true , value = "pessoaId") Long pessoaId, @RequestParam(required = false) Map<String, String> requestParams, @PathVariable("page") int page, @PathVariable("length") int length) {
		
		// http://{{SERVER}}:8080/administracao/conta/all/1/10?order=desc&resumir=false
		// http://{{SERVER}}:8080/administracao/conta/all/1/10?order=desc&resumir=true

		try {
			
			Long count = contaRepository.count(pessoaId, null, null, entityManagerFactory);
			if (count > 0) {
				List<Conta> contas = contaRepository.findAll(pessoaId, entityManagerFactory, requestParams, page, length);
				
				// aqui a idéia é de que poderia vir do front end um radio selecionado para em uma consulta, 
				// resumir o retorno do objeto, então poderir vir uma request com uma url semelhante a esta :
				// http://{{SERVER}}:{{GATEWAY_PORT}}/st-api-cadastro/administracao/pessoa/all/1/10?order=desc&resumir=true
				
				HttpHeaders headers = httpHeaders(count.toString());
				Boolean resumed = Boolean.valueOf(StringUtils.equalsAny("true", requestParams.get("resumir")));
				
				return ResponseEntity.ok()
				        .headers(headers)
				        .body(resumed ? contaMapper.toResumeDTO(contas) : contaMapper.toDTO(contas));
			}
			return ResponseEntity.ok().headers(httpHeaders(count.toString())).body(EMPTY_MSG);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}
	
	@Operation(summary = "Search all Account by filters pagened")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the List of Account", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cartao.class))) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Pessoa", description = "Get Account by filters")
	@PostMapping(value = "/findByFilters/{page}/{length}", produces = "application/json" )
	public ResponseEntity<Object> findByFilters(@RequestHeader(value ="pessoaId", required= true) Long pessoaId, @RequestParam(required = false) Map<String, String> requestParams, @PathVariable("page") int page, @PathVariable("length") int length, @RequestBody ContaDTO dto) {

		try {
			
			Long count = contaRepository.count(pessoaId, null, dto, entityManagerFactory);
			
			if (count > 0) {
				List<Conta> contas = contaRepository.findByFilters(pessoaId, dto, entityManagerFactory, requestParams, page, length);
				HttpHeaders headers = httpHeaders(count.toString());
				return ResponseEntity.ok()
						.headers(headers)
						.body(contaMapper.toDTO(contas));
			}
			return ResponseEntity.ok().headers(httpHeaders(count.toString())).body(EMPTY_MSG);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}
	
	@Operation(summary = "Search all Account by filters pagened")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the List of Account", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cartao.class))) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Pessoa", description = "Get Account by filters")
	@PostMapping(value = "/findByAccount/{page}/{length}", produces = "application/json" )
	public ResponseEntity<Object> findByAccount(@RequestHeader(value ="pessoaId", required= true) Long pessoaId, @RequestParam(required = false) Map<String, String> requestParams, @PathVariable("page") int page, @PathVariable("length") int length, @RequestBody ContaDTO dto) {

		try {
			
			Long count = contaRepository.count(pessoaId, null, dto, entityManagerFactory);
			
			if (count > 0) {
				List<Conta> contas = contaRepository.findByFilters(pessoaId, dto, entityManagerFactory, requestParams, page, length);
				HttpHeaders headers = httpHeaders(count.toString());
				return ResponseEntity.ok()
						.headers(headers)
						.body(contaMapper.toDTO(contas));
			}
			return ResponseEntity.ok().headers(httpHeaders(count.toString())).body(EMPTY_MSG);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}
	
	private CommonsValidation createCommonsValidation() {

		return CommonsValidation.builder()
				.cartaoRepository(new CartaoRepository())
				.build();
	}
	
	private ResponseEntity<Object> mountEntity(ContaDTO dto, Long id, Pessoa pessoa, boolean update) throws CartaoException {

		try {

			Conta entity = contaMapper.toEntity(dto);
			
			HttpHeaders headers = new HttpHeaders();
			
			if (isDisableAccount(dto.getInativada()))
				entity.setInativada(true);
			
			entity.setId(update ? id : null);
			entity.setPessoa(pessoa);
			entity = contaRepository.save(entity);
			headers.add("Location", "/find/" + entity.getId());
			return new ResponseEntity<>(entity, headers, update ? HttpStatus.NO_CONTENT : HttpStatus.CREATED);
		} catch (NoResultException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		} catch (Exception ex) {
			throw new CartaoException(ex.getMessage(), ex);
		}
	}
	
	private boolean isDisableAccount(Boolean disable) {
		return disable.equals(true);
	}
	
	public ContaController(ObjectMapper mapperJson, PessoaRepository pessoaRepository, CartaoRepository cartaoRepository , ContaRepository contaRepository ) {
		super(mapperJson, pessoaRepository, cartaoRepository, contaRepository);
	}

}
