package br.com.cadastroit.sevices.web.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.cadastroit.services.api.domain.Conta;
import br.com.cadastroit.services.api.enums.TipoPessoa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaDTO  {

    private Long id;

    @NotNull(message = "Campo nome sem preenchimento")
	@Size(min = 1, max = 60, message = "Campo nome nao pode ultrapassar o total de 60 caracteres.")
	private String nome;
	
	private Date dataNascimento;
	
	private TipoPessoa tipoPessoa;
	
	private EnderecoDTO endereco;

    private JuridicaDTO juridica;
    
    private FisicaDTO fisica;
    
    private List<Conta> contas;

   
}