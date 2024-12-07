package br.com.cadastroit.services.api.enums;
public enum CartaoStatus {

	BLOQUEADO("Atendido"),
	CANCELADO("Cancelado"),
	PENDENTE_APROVACAO("Pendente de aprovação"),
	PARCIALMENTE_APROVADO("Parcialmente aprovado"),
	APROVADO("Aprovado"),
	ENTREGUE("Entregue"),;

	private String descricao;

	CartaoStatus(String descricao) {

		this.descricao = descricao;
	}

	public String getDescricao() {

		return descricao;
	}

}
