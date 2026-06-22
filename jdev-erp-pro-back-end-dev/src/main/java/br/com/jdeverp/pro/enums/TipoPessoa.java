package br.com.jdeverp.pro.enums;

public enum TipoPessoa {

	PESSOA_FISICA("Plano Gratuito"), 
	PESSOA_JURIDICA("Pessoa Jurídica");

	
	private final String descricao;
	
	private TipoPessoa(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

}
