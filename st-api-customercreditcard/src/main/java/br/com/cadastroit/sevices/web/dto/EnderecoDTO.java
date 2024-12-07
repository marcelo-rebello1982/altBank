package br.com.cadastroit.sevices.web.dto;
import br.com.cadastroit.services.api.domain.Logradouro;
import br.com.cadastroit.services.api.enums.TipoEndereco;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDTO  {

	private Long id;

	private String numero;

	private String complemento;

	private String bairro;
	
	private TipoEndereco tipoEndereco;

	private Logradouro logradouro;
	
}
