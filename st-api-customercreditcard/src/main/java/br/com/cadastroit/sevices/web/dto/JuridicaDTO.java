package br.com.cadastroit.sevices.web.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JuridicaDTO  {
	
	private Long id;
	
	@NotNull(message = "Campo Número CNPJ sem preenchimento!")
	private BigDecimal numCnpj;
	
	private PessoaDTO pessoa;
	
}