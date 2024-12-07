package br.com.cadastroit.services.api.enums;

import java.util.Arrays;
import java.util.List;

public enum TipoPessoa {

	FISICA(0, "Fisica"),
	JURIDICA(1, "Juridica");

	private int tipo;
	private String descricao;

	private TipoPessoa(int tipo, String descricao) {

		this.tipo = tipo;
		this.descricao = descricao;
	}

	public int getTipo() {

		return tipo;
	}

	public String getDescricao() {

		return descricao;
	}

	public static List<TipoPessoa> getOpcoesTipoPessoa() {
		return Arrays.asList(new TipoPessoa[] { FISICA, JURIDICA });
	}
	
}
