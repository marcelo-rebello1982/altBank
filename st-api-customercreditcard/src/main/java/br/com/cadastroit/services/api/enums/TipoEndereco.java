package br.com.cadastroit.services.api.enums;
public enum TipoEndereco {

	RES("Residencial"),
	CORRESPONDENCIA("Correspondência"), 
	COBRANCA("Cobrança"),
	ENTREGA("Entrega");

	private String descricao;

	TipoEndereco(String descricao) {

		this.descricao = descricao;
	}

	public String getDescricao() {

		return descricao;
	}
}
