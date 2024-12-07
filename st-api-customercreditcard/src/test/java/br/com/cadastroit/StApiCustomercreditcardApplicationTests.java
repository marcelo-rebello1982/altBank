package br.com.cadastroit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.stream.Stream;

import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.cadastroit.services.api.domain.Conta;
import br.com.cadastroit.services.api.domain.Pessoa;
import br.com.cadastroit.services.api.domain.Pessoa_;
import br.com.cadastroit.services.exceptions.ContaException;
import br.com.cadastroit.services.repositories.CartaoRepository;
import br.com.cadastroit.services.repositories.ContaRepository;
import br.com.cadastroit.services.repositories.PessoaRepository;
import br.com.cadastroit.services.web.mapper.CartaoMapper;
import br.com.cadastroit.services.web.mapper.ContaMapper;
import br.com.cadastroit.services.web.mapper.PessoaMapper;
import br.com.cadastroit.sevices.web.dto.ContaDTO;
import br.com.cadastroit.sevices.web.dto.EnderecoDTO;
import br.com.cadastroit.sevices.web.dto.PessoaDTO;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@Slf4j
class StApiCustomercreditcardApplicationTests {

	private final String URL_CARTAO = "/administracao/cartao";
	private final String URL_PESSOA = "/administracao/pessoa";
	private final String URL_CONTA = "/administracao/conta";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	private Long maxId;
	
	private Long cartaoId;
	
	private Long contaId;
	
	private Long pessoaId;
	
	private final String passwordAuth = "Bearer Y3N0LWFkbWluLTIweHgjMTokMnkkMTIkdnVTUk1CNFRmOHpMdVhvLkdmeDVXZU5QMGttWWxRNXpOUzFPMHJJb3hSLmgzQ3lNUnN3b2k=";
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Autowired
	private PessoaMapper pessoaMapper = Mappers.getMapper(PessoaMapper.class);
	private ContaMapper contaMapper = Mappers.getMapper(ContaMapper.class);
	private CartaoMapper cartaoMapper = Mappers.getMapper(CartaoMapper.class);
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private ContaRepository contaRepository;
	
	@Autowired
	private CartaoRepository cartaoRepository;
	
	@BeforeEach
	public void beforeEach() throws Exception {
		
		String result = this.mockMvc.perform(get(URL_PESSOA + "/maxId").header("bancoId", "1")
				.header("Authorization",
						"Bearer Y3N0LWFkbWluLTIweHgjMTokMnkkMTIkdnVTUk1CNFRmOHpMdVhvLkdmeDVXZU5QMGttWWxRNXpOUzFPMHJJb3hSLmgzQ3lNUnN3b2k=")
				.accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		this.pessoaId = new Long(result.isEmpty() ? "1" : result);
		
	}
	
	
	@Test
	@Order(1)
	void handleTestBlockException() {
		Conta conta = Conta.builder().pessoa(Pessoa.builder().id(000L).build()).build();
		RuntimeException runtimeException = assertThrows(RuntimeException.class,
				() -> this.contaRepository.save(conta));
		assertThat(runtimeException.getMessage()).isNotNull();
		log.info("handleTestBlockException EXECUTED => " + runtimeException.getMessage());
	}
	
	@Test
	@Order(1)
	void handlePostConta() throws Exception {
		Conta conta = Conta.builder().agencia(330).numero(13456).saldo(new BigDecimal(16545.45))
				.saldo(new BigDecimal(100.00)).inativada(false).pessoa(Pessoa.builder().id(3L).build()).build();
		
		ContaDTO contaDTO = this.contaMapper.toDTO(conta);

		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(contaDTO);

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.request(HttpMethod.POST,
				new URI(URL_CONTA + "/create"));

		builder.contentType(MediaType.APPLICATION_JSON_VALUE)
						.header("pessoaId", 3L)
						.content(json);

		String result = this.mockMvc.perform(builder)
				
				.andExpect(status().isCreated()).andReturn().getResponse()
				.getContentAsString();
		log.info("handlePostConta EXECUTED => " + result);
	}
	
	@Test
	@Order(2)
	void handleUpdateConta() throws Exception {
		Conta conta = this.contaRepository.findById(this.contaRepository.maxId(entityManagerFactory)).get();
		ContaDTO contaDto = this.contaMapper.toDTO(conta);
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(contaDto);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.request(HttpMethod.PUT,
				new URI(URL_CONTA + "/update/" + this.contaRepository.maxId(entityManagerFactory)));
		builder.contentType(MediaType.APPLICATION_JSON_VALUE).content(json);
		String result = this.mockMvc.perform(builder).andExpect(status().isNoContent()).andReturn().getResponse()
				.getContentAsString();
		log.info("UpdateUpdateConta EXECUTED => " + result);
	}

	@Test
	@Order(3)
	void handleUpdateContaNotExists() throws Exception {

		String capException = "";
		try {
			Conta conta = Conta.builder().id(0L).build();
			ContaDTO contaDto = this.contaMapper.toDTO(conta);
			mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(contaDto);
			MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.request(HttpMethod.PUT, new URI(
					URL_CONTA + "/update/" + this.contaRepository.maxId(entityManagerFactory)));
			builder.contentType(MediaType.APPLICATION_JSON_VALUE).content(json);
			String result = this.mockMvc.perform(builder).andExpect(status().isBadRequest()).andReturn().getResponse()
					.getContentAsString();
			log.info("handleUpdateContaNotExists EXECUTED => " + result);
		} catch (ContaException e) {
			capException = e.getMessage();
		}
		log.info("handleUpdateContaNotExists NOTEXISTS EXECUTED => " + capException);
		assertEquals(true, capException != null);
	}

	@Test
	@Order(4)
	void handleUpdateContaIsBadRequest() throws Exception {

		String capException = "";
		try {

			ContaDTO contaDto = this.contaMapper.toDTO(this.contaRepository
					.findById(this.contaRepository.maxId(entityManagerFactory)).get());

			mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(contaDto);
			MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.request(HttpMethod.PUT, new URI(
					URL_CONTA + "/update/" + this.contaRepository.maxId(entityManagerFactory)));
			builder.contentType(MediaType.APPLICATION_JSON_VALUE).content(json);

			String result = String.valueOf(this.mockMvc.perform(builder).andExpect(status().isBadRequest()).andReturn()
					.getResponse().getStatus());
			assertEquals(true, result.contains("400"));
			log.info("UpdateParamGeraRegSubApurIcmsisBadRequest  EXECUTED => " + capException);
		} catch (ContaException e) {
			capException = e.getMessage();
		}
	}

	@Test
	@Order(5)
	void findAllConta() throws Exception {
		try {
			String result = this.mockMvc
					.perform(get(URL_CONTA + "/all/1/10").accept(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
			log.info("findAllConta EXECUTED => " + result);
			assertEquals(true, result != null);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Test
	@Order(6)
	void findMaxIdConta() throws Exception {
		try {
			String result = this.mockMvc
					.perform(get(URL_CONTA + "/maxId/").accept(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
			log.info("findMaxIdConta EXECUTED => " + result);
			assertEquals(true, result != null);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Test
	@Order(7)
	void findByIdConta() throws Exception {
		try {
			String result = this.mockMvc.perform(
					get(URL_CONTA + "/find/" + this.contaRepository.maxId(entityManagerFactory))
							.accept(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
			log.info("findByIdConta EXECUTED => " + result);
			assertEquals(true, result != null);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Test
	@Order(8)
	void findByIdContaNotExists() throws Exception {
		try {
			String result = this.mockMvc
					.perform(get(URL_CONTA + "/find/" + 000101).accept(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
			log.info("findByIdContaNotExists EXECUTED => " + result);
			assertEquals(true, result != null);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Test
	@Order(8)
	void handlePostContaNotExists() throws Exception {
		String capException = "";
		try {
			Conta conta = Conta.builder().id(0L).build();
			ContaDTO contaDto = this.contaMapper.toDTO(conta);
			mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(contaDto);
			MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.request(HttpMethod.POST,
					new URI(URL_CONTA + "/create"));
			builder.contentType(MediaType.APPLICATION_JSON_VALUE).content(json);

			capException = this.mockMvc.perform(builder).andExpect(status().is4xxClientError()).andReturn()
					.getResponse().getContentAsString();
		} catch (ContaException e) {
			capException = e.getMessage();
		}
		log.info("Post handlePostContaNotExists NOTEXISTS EXECUTED => " + capException);
		assertEquals(true, capException != null);
	}

	@Test
	@Order(9)
	void checkingJsonConta() throws Exception {

		try {

			Conta conta = this.contaRepository
					.findById(this.contaRepository.maxId(entityManagerFactory)).get();
			ContaDTO contaDto = this.contaMapper.toDTO(conta);
			log.info("Checking Entity Item...");
			mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(contaDto);
			log.info("Value => " + json);
			log.info("Checking getter values checkingJsonConta  => executed");
			this.invokeGetter(contaDto);
			assertEquals(true, json != null);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	@Test
	@Order(11)
	void buscarPorFiltrosConta() throws Exception {
		try {
			Conta conta = this.contaRepository
					.findById(this.contaRepository.maxId(entityManagerFactory)).get();
			ContaDTO contaDto = this.contaMapper.toDTO(conta);
			mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(contaDto);
			MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.request(HttpMethod.POST,
					new URI(URL_CONTA + "/findByFilters/1/10?order=desc&resume=false"));
			builder.contentType(MediaType.APPLICATION_JSON_VALUE).content(json);
			String result = this.mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse()
					.getContentAsString();
			log.info("buscarPorFiltrosConta EXECUTED " + result);
			log.info(result);
			assertEquals(true, result != null);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Test
	@Order(12)
	void buscarPorFiltrosContaIsEmpty() throws Exception {
		try {
			ContaDTO contaDto = ContaDTO.builder().id(0L)
					.build();
			mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(contaDto);
			MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.request(HttpMethod.POST,
					new URI(URL_CONTA + "/findByFilters/1/10"));
			builder.contentType(MediaType.APPLICATION_JSON_VALUE).content(json);
			boolean result = this.mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse()
					.getContentAsString().equals("List is empty...");
			log.info("buscarPorFiltrosContaIsEmpty EXECUTED " + result);
			log.info(Boolean.toString(result));
			assertEquals(true, result);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Test
	@Order(13)
	void handleDeleteContaNotExists() throws Exception {
		String result = this.mockMvc
				.perform(delete(URL_CONTA + "/delete/" + 0))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		log.info("DELETE handleDeleteContaNotExists NOT-EXISTS EXECUTED " + result);
		assertEquals(true, result != null);
	}
	
	
	@Test
	@Order(14)
	void findAllPessoa() throws Exception {
		try {
			String result = this.mockMvc.perform(get(URL_PESSOA + "/all/1/10").header("bancoId", "1")
					.header("hash", "c4ca4238a0b923820dcc509a6f75849b")
					.header("Authorization",
							"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtcC5yZWJlbGxvLm1hcnRpbnMiLCJpYXQiOjE3MzExMTgxODIsImV4cCI6MTczMTk4MjE4Mn0.5eOgTyUqIjSLl7pocI5qpyJEF2nc0SuhaecqKEVBHDYY29VnU1U2B8rUEJXiDTmgOFMhYSwptYwOjFErsFqLPQ\"")
					.accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn().getResponse()
					.getContentAsString();
			log.info(result);
			assertEquals(true, result != null);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}
	
	@Test
	@Order(15)
	void findPessoaById() throws Exception {
		try {
			String result = this.mockMvc.perform(get(URL_PESSOA + "/find/" + (this.pessoaId)).header("bancoId", "1")
					.header("hash", "c4ca4238a0b923820dcc509a6f75849b")
					.header("Authorization",
							"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtcC5yZWJlbGxvLm1hcnRpbnMiLCJpYXQiOjE3MzExMTgxODIsImV4cCI6MTczMTk4MjE4Mn0.5eOgTyUqIjSLl7pocI5qpyJEF2nc0SuhaecqKEVBHDYY29VnU1U2B8rUEJXiDTmgOFMhYSwptYwOjFErsFqLPQ")
					.accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn().getResponse()
					.getContentAsString();
			log.info(result);
			assertEquals(true, result != null);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Test
	@Order(16)
	void handleDeletePessoa() throws Exception {
		String result = this.mockMvc
				.perform(delete(URL_PESSOA + "/delete/" + this.pessoaId).header("bancoId", "1").header("Authorization",
						"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtcC5yZWJlbGxvLm1hcnRpbnMiLCJpYXQiOjE3MzExMTgxODIsImV4cCI6MTczMTk4MjE4Mn0.5eOgTyUqIjSLl7pocI5qpyJEF2nc0SuhaecqKEVBHDYY29VnU1U2B8rUEJXiDTmgOFMhYSwptYwOjFErsFqLPQ\"")
						.header("hash", "c4ca4238a0b923820dcc509a6f75849b"))
				.andExpect(status().isNoContent()).andReturn().getResponse().getContentAsString();
		log.info(result);
	}
	
	@Test
	@Order(17) // aqui apenas testa as validações do Bean Validation @Size
	void handlePostPessoaAndReturnMaxSizeErrorAnotation() throws Exception {

		PessoaDTO entityDto = PessoaDTO.builder()
				.endereco(EnderecoDTO.builder().complemento(RandomStringUtils.secure().next(1000, true, true)).build())
				.build();

		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(entityDto);

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.request(HttpMethod.POST,
				new URI(URL_PESSOA + "/create"));
		builder.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header("pessoaId", pessoaId)
				.header("Authorization", passwordAuth)
				.content(json);
		String result = this.mockMvc.perform(builder).andExpect(status().isBadRequest())
				.andExpect(s -> assertEquals(true, s.getResponse().getContentAsString().contains("Size"))).andReturn()
				.getResponse().getContentAsString();
		log.info("handlePostPessoaAndReturnMaxSizeErrorAnotation EXECUTED => " + result);
	}
	
	@Test
	@Order(18) // aqui apenas testa as validações do Bean Validation @NotNull
	void handleUpdatePessoaAndReturnErrorFieldsWithAnnotationNotNull() throws Exception {

		try {
			PessoaDTO entityDto = PessoaDTO.builder().id(0L).build();
			mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(entityDto);
			MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.request(HttpMethod.PUT,
					new URI(URL_PESSOA + "/update/" + pessoaId));
			builder.contentType(MediaType.APPLICATION_JSON_VALUE)
					.header("Authorization", passwordAuth)
					.content(json);
			String result = this.mockMvc.perform(builder).andExpect(status().isBadRequest())
					.andExpect(r -> assertTrue(r.getResponse().getContentAsString().contains("NotNull"))).andReturn()
					.getResponse().getContentAsString();
			log.info("handleUpdatePessoaAndReturnErrorFieldsWithAnnotationNotNull  EXECUTED => " + result);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}
	
	@Test
	@Order(19)
	void findByFiltersIsEmpty() throws Exception {
		
		try {
			
			// aqui a idéia é quando uma busca por um determinado filtro não retorna
			// nenhum objeto, é retornado apenas uma mensagem para o frontEnd que podera
			// trata-la de acordo com a necessidade.
			
			PessoaDTO entityDto = PessoaDTO.builder().endereco(EnderecoDTO.builder().complemento("XXXYYYZZZ").build()).build();

			mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(entityDto);
			MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.request(HttpMethod.POST,
					new URI(URL_PESSOA + "/findByFilters/1/10?order=desc&resume=false"));
			builder.contentType(MediaType.APPLICATION_JSON_VALUE)
					.header("Authorization", passwordAuth)
					.content(json);
			boolean result = this.mockMvc.perform(builder).andExpect(status().isOk())
					.andExpect(
							r -> assertEquals(true, r.getResponse().getContentAsString().contains("List is empty...")))
					.andReturn().getResponse().getContentAsString().equals("List is empty...");
			log.info("findByFiltersIsEmpty => executed " + result);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	// @Test
	// @Order(15) somente se estiver com security
	void handlePostContaIsUnauthorized() throws Exception {
		String result = this.mockMvc.perform(delete(URL_CONTA + "/delete/" + 0)).andReturn().getResponse().getContentAsString();
		log.info("POST handleDeleteContaNotExists IsUnauthorized EXECUTED " + result);
		assertEquals(true, result != null);
	}
	
	private void invokeGetter(Object... o) {
		Stream.of(o).forEach(obj -> {
			log.info("Object Name = " + obj.getClass().getSimpleName());
			Stream.of(obj.getClass().getDeclaredMethods()).filter(m -> m.getName().contains("get")).forEach(m -> {
				try {
					if (m.invoke(obj) != null)
						log.info(m.invoke(obj).toString());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					log.error("Error on execution method,[error] = " + e.getMessage());
				}
			});
		});
	}
	
	
}
