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
public class ContaDTO  {

	private Long id;

	@NotNull(message = "Campo agencia sem preenchimento")
    private int agencia;

	@NotNull(message = "Campo numero sem preenchimento")
	private int numero;
	
    private BigDecimal saldo;
    
    private BigDecimal limiteCredito;
    
    private Boolean inativada;

	@NotNull(message = "Campo pessoa sem preenchimento")
    private PessoaDTO pessoa;

}