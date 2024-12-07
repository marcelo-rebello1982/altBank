package br.com.cadastroit.sevices.web.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaDisabledDTO  {
	
	private Boolean inativada;

	@Builder.Default
	private String mensagem = "Conta Desabilitada";

}