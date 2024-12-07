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
import br.com.cadastroit.services.api.domain.Pessoa;
import br.com.cadastroit.services.api.enums.CartaoStatus;
import br.com.cadastroit.services.exceptions.CartaoException;
import br.com.cadastroit.services.repositories.CartaoRepository;
import br.com.cadastroit.services.repositories.ContaRepository;
import br.com.cadastroit.services.repositories.PessoaRepository;
import br.com.cadastroit.services.web.controller.commons.CommonsController;
import br.com.cadastroit.services.web.controller.commons.CommonsValidation;
import br.com.cadastroit.sevices.web.dto.CartaoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/administracao/cartao")
public class CartaoController extends CommonsController {
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Operation(summary = "Get Max Id from Cartao")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Credit Card created with sucessful", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = CartaoDTO.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Cartao", description = "Get a Credit Card max id record")
	@GetMapping("/maxId")
	public ResponseEntity<Object> maxId(@RequestHeader("pessoaId") Long pessoaId) {

		try {
			Long id = cartaoRepository.maxId(entityManagerFactory, pessoaId);
			return ResponseEntity.ok(id);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body((String.format(ex.getMessage())));
		}
	}
	
	@Operation(summary = "Get a CreditCard by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the CreditCard", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Cartao.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "CreditCard not found", content = @Content) })
	@Tag(name = "CreditCard", description = "Get a CreditCard record by id")
	@GetMapping(value = "/find/{id}", produces = "application/json" )
	public ResponseEntity<Object> find(@RequestHeader("pessoaId") Long pessoaId, @PathVariable("id") Long id) {

		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			Cartao entity = cartaoRepository.findById(pessoaId, id, entityManagerFactory);
			return ResponseEntity.ok().body(cartaoMapper.toDTO(entity));
		} catch (Exception ex) {
			// return ResponseEntity.notFound() ?
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Operation(summary = "Create CreditCard")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "CreditCard created with sucessful", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = CartaoDTO.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Order", description = "Create a new CreditCard record")
	@PostMapping("/create")
	public ResponseEntity<Object> handlePost(@RequestHeader(required = false , value = "pessoaId") Long pessoaId, @Validated @RequestBody CartaoDTO dto) {

		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			Pessoa entity = pessoaRepository.findById(pessoaId, entityManagerFactory);
			return mountEntity(dto, pessoaId, entity, false);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Tag(name = "Credit Card", description = "Updating a Credit Card record")
	@PutMapping("/update/{id}")
	public ResponseEntity<Object> handleUpdate(@RequestHeader(required =  false , value = "pessoaId") Long pessoaId, @PathVariable("id") Long id, @Validated @RequestBody CartaoDTO dto) {

		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			Pessoa entity = pessoaRepository.findById(pessoaId, entityManagerFactory);
			return this.mountEntity(dto, id, entity, true);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Tag(name = "Virtal Card", description = "Request Virtal Card")
	@PutMapping("/requestVirtualCard/{id}")
	public ResponseEntity<Object> handleRequesVirtualCard(@RequestHeader(required =  false , value = "pessoaId") Long pessoaId, @PathVariable("id") Long id, @Validated @RequestBody CartaoDTO dto) {

		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			
			Cartao cartao = cartaoRepository.findById(pessoaId, id, entityManagerFactory);
			
			if (!isStatusCartao(cartao, CartaoStatus.ENTREGUE)) 
				
				return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format("CARTÃO NÃO ENTREGUE/VALIDADO")));
			return ResponseEntity.ok().body(commonsValidation.createResponseTemplate(String.format("CARTÃO VIRUTAL SOLICITADO")));
			
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Operation(summary = "Delete Credit Card By id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "No Content", description = "Update Credit Card", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Order", description = "Deleting a Credit Card record")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> delete(@PathVariable("id") Long id) throws CartaoException {

		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			cartaoRepository.delete(cartaoRepository.findById(id, entityManagerFactory));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Operation(summary = "Search all Credit Card")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the List of Credit Card", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cartao.class))) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Order", description = "Get all records using pagination mechanism")
	@GetMapping("/all/{page}/{length}")
	public ResponseEntity<Object> all(@RequestHeader(required = true , value = "pessoaId") Long pessoaId, @RequestHeader(required = true , value = "contaId") Long contaId , @RequestParam(required = false) Map<String, String> requestParams, @PathVariable("page") int page, @PathVariable("length") int length) {

		try {
			
			Long count = cartaoRepository.count(pessoaId, contaId, null, entityManagerFactory);
			if (count > 0) {
				List<Cartao> cartoes = cartaoRepository.findAll(pessoaId, entityManagerFactory, requestParams, page, length);
				
				// aqui a idéia é de que poderia vir do front end um radio selecionado para em uma consulta, 
				// resumir o retorno do objeto, então poderir vir uma request com uma url semelhante a esta :
				// http://{{SERVER}}:{{GATEWAY_PORT}}/st-api-cadastro/administracao/pessoa/all/1/10?order=desc&resumir=true
				
				HttpHeaders headers = httpHeaders(count.toString());
				Boolean resumir = Boolean.valueOf(StringUtils.equalsAny("true", requestParams.get("resumir")));
				
				return ResponseEntity.ok()
				        .headers(headers)
				        .body(resumir ? cartaoMapper.toResumeDTO(cartoes) : cartaoMapper.toDTO(cartoes));
			}
			return ResponseEntity.ok().headers(httpHeaders(count.toString())).body(EMPTY_MSG);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}
	
	@Operation(summary = "Search all Credit Card by filters pagened")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the List of Credit Card", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cartao.class))) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Pessoa", description = "Get Credit Card by filters")
	@PostMapping("/findByFilters/{page}/{length}")
	public ResponseEntity<Object> findByFilters(@RequestHeader(value ="pessoaId", required= true) Long pessoaId, @RequestParam(required = false) Map<String, String> requestParams, @PathVariable("page") int page, @PathVariable("length") int length, @RequestBody CartaoDTO dto) {

		try {
			
			Long count = cartaoRepository.count(pessoaId, null, dto, entityManagerFactory);
			
			if (count > 0) {
				List<Cartao> cartoes = cartaoRepository.findByFilters(pessoaId, dto, entityManagerFactory, requestParams, page, length);
				
				HttpHeaders headers = httpHeaders(count.toString());
				
				return ResponseEntity.ok()
						.headers(headers)
						.body(cartaoMapper.toDTO(cartoes));
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
	
	private ResponseEntity<Object> mountEntity(CartaoDTO dto, Long id, Pessoa pessoa, boolean update) throws CartaoException {

		try {
			
			if (isStatusCartao(dto.getStatus())) 
				dto.setEntregueValidado(true);

			Cartao entity = cartaoMapper.toEntity(dto);
			HttpHeaders headers = new HttpHeaders();
			entity.setId(update ? id : null);
			entity.setPessoa(pessoa);
			entity = cartaoRepository.save(entity);
			headers.add("Location", "/find/" + entity.getId());
			return new ResponseEntity<>(entity, headers, update ? HttpStatus.NO_CONTENT : HttpStatus.CREATED);
		} catch (NoResultException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		} catch (Exception ex) {
			throw new CartaoException(ex.getMessage(), ex);
		}
	}
	
	private boolean isStatusCartao(CartaoStatus status) {
		return CartaoStatus.ENTREGUE.equals(status);
	}
	
	private boolean isStatusCartao(Cartao cartao, CartaoStatus status) {
		return cartao.getStatus().equals(status.ENTREGUE);
	}
	
	public CartaoController(ObjectMapper mapperJson, PessoaRepository pessoaRepository, CartaoRepository cartaoRepository, ContaRepository contaRepository ) {
		super(mapperJson, pessoaRepository, cartaoRepository, contaRepository);
	}

}
