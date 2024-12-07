package br.com.cadastroit.services.web.controller.commons;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.cadastroit.services.api.enums.DbLayerMessage;
import br.com.cadastroit.services.exceptions.GenericException;
import br.com.cadastroit.services.repositories.CartaoRepository;
import br.com.cadastroit.services.repositories.commons.CommonsRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Builder
@AllArgsConstructor
public class CommonsValidation {
	
	private final ObjectMapper mapperJson = new ObjectMapper();
	
	private CommonsRepository commonsRepository;
	
	private CartaoRepository cartaoRepository;
	
	public <T> T recuperarObjeto(Long id, Class<T> clazz, EntityManagerFactory entityManagerFactory) {
		try {
			return this.commonsRepository.findById(id, clazz, entityManagerFactory);
		} catch (NoResultException | NonUniqueResultException ex) {
			throw new GenericException(
					String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), clazz.getName().toUpperCase(), "", id));
		}
	}

	public String createResponseTemplate(String message) {
	    try {
	        ResponseTemplate responseTemplate = ResponseTemplate.builder().message(message).build();
	        return convertToJson(responseTemplate, responseTemplate.getClass());
	    } catch (JsonProcessingException ex) {
	        log.error(ex.getMessage());
	        return null;
	    }
	}

	public String convertToJson(Object data, Class<? extends ResponseTemplate> clazz) throws JsonProcessingException {
		mapperJson.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapperJson.writer().withDefaultPrettyPrinter();
		return ow.writeValueAsString(data);
	}
}
