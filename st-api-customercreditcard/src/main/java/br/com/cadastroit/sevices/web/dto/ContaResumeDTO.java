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
public class ContaResumeDTO  {

	@NotNull(message = "Campo agencia sem preenchimento")
    private int agencia;

	@NotNull(message = "Campo numero sem preenchimento")
	private int numero;

}