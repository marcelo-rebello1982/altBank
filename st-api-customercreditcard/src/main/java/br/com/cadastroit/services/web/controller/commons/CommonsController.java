package br.com.cadastroit.services.web.controller.commons;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cadastroit.services.api.enums.DbLayerMessage;
import br.com.cadastroit.services.repositories.CartaoRepository;
import br.com.cadastroit.services.repositories.ContaRepository;
import br.com.cadastroit.services.repositories.PessoaRepository;
import br.com.cadastroit.services.web.mapper.CartaoMapper;
import br.com.cadastroit.services.web.mapper.ContaMapper;
import br.com.cadastroit.services.web.mapper.PessoaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommonsController {
	
	@Autowired
	public EntityManagerFactory entityManagerFactory;

	@Autowired
	protected final ObjectMapper mapperJson;
	
	protected final PessoaRepository pessoaRepository;
	
	protected final CartaoRepository cartaoRepository;
	
	protected final ContaRepository contaRepository;
	
 	protected final CartaoMapper cartaoMapper = Mappers.getMapper(CartaoMapper.class);
 	
 	protected final PessoaMapper pessoaMapper = Mappers.getMapper(PessoaMapper.class);
 	
 	protected final ContaMapper contaMapper = Mappers.getMapper(ContaMapper.class);
 	
	protected final String EMPTY_MSG = "List is empty...";
	
	protected HttpHeaders httpHeaders(String count) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("summaryCount", count);
		return headers;
	}
	
	public <T> T convertToObject(String json, Class<T> clazz) throws JsonProcessingException {
		return mapperJson.readValue(json, clazz);
	}
	
	public boolean validateStatus(BigDecimal status) {
		return new HashSet<>(Arrays.asList(4L, 6L, 7L, 8L))
			.contains(status.longValue());
	}
	
	protected ResponseEntity<Object> validarCollection(List<?> collection) {
		return collection.isEmpty()
				? ResponseEntity.status(HttpStatus.NOT_FOUND).body(DbLayerMessage.EMPTY_MSG.message())
				: ResponseEntity.status(HttpStatus.OK).body(collection);
	}
}
