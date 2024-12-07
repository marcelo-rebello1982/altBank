package br.com.cadastroit.sevices.web.dto;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import br.com.cadastroit.services.api.enums.CartaoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartaoDTO  {

    private Long id;
    
	@NotNull(message = "Campo numeroCartao sem preenchimento")
    private String numeroCartao;
   
	@NotNull(message = "Campo bandeira sem preenchimento")
    private String bandeira;

    private BigDecimal limiteCredito;

	@NotNull(message = "Campo nomeTitular sem preenchimento")
    private String nomeTitular;

	@NotNull(message = "Campo codigoSeguranca sem preenchimento")
    private BigDecimal codigoSeguranca;
    
	@Builder.Default
    private Boolean ativo = false;
    
	@Builder.Default
    private Boolean bloqueado = true;
	
	@Builder.Default
    private Boolean entregueValidado = false;
    
	private CartaoStatus status;
    
    private PessoaDTO pessoa;
    
	private List<CartaoPessoaDTO> cartoesPessoa;
    
}
