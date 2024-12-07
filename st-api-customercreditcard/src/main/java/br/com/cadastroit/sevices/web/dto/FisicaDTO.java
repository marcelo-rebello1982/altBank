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
public class FisicaDTO {
	
	private Long id;
	
	@NotNull(message = "Campo Número CPF sem preenchimento!")
	private BigDecimal numCpf;
	
	private String rg;
	
	
}