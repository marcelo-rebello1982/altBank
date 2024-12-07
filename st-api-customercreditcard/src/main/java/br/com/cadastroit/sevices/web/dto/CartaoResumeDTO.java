package br.com.cadastroit.sevices.web.dto;
import br.com.cadastroit.services.api.enums.CartaoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartaoResumeDTO  {

    private Long id;
    
    private String numeroCartao;
   
    private String bandeira;

    private Boolean bloqueado = true;
	
    private Boolean entregueValidado = false;
    
	private CartaoStatus status;
    
}
