package br.com.cadastroit.sevices.web.dto;
import org.springframework.util.Assert;

import br.com.cadastroit.services.exceptions.BusinessException;


public enum Logradouro {

	AVENIDA("Avenida"),
	CAMPO("Campo"),
	CHACARA("Chácara"),
	CONDOMINIO("Condomínio"),
	ESTRADA("Estrada"),
	FAZENDA("Fazenda"),
	LOTEAMENTO("Loteamento"),
	RODOVIA("Rodovia"),
	RUA("Rua"),
	SITIO("Sítio"),
	VILA("Vila");

	Logradouro(String descricao) {

		this.descricao = descricao;
	}
	
	private String descricao;
	
	public String getDescricao() {
	
		return descricao;
	}
	
	public static Logradouro fromString(String descricao) {

		Assert.notNull(descricao, "logradouro nulo");

		for (Logradouro logradouro : Logradouro.values()) {
			if (descricao.equalsIgnoreCase(logradouro.getDescricao()))
				return logradouro;
		}

		throw new BusinessException("Logradouro não encontrado");
	}
	
}
