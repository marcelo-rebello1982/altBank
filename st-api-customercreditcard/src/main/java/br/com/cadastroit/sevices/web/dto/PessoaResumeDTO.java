package br.com.cadastroit.sevices.web.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaResumeDTO  {

    private Long id;

	private String nome;
	
	private Date dataNascimento;
   
}