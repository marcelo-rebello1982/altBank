package br.com.cadastroit.sevices.web.dto;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartaoPessoaDTO {

	private Long id;

	private Integer nroPedidoCartao;
	
    private BigDecimal limiteMinimo;

	private PessoaDTO pessoa;
	
	private CartaoDTO cartao;
	
}
