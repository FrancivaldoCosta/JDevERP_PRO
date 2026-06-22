package br.com.jdeverp.pro.enums;

public enum TipoPlano {
	
	FREE("Plano Gratuito"),
	STARTER("Plano starter"),
	BASIC("Plano básico"),
	PRO("Plano profissional"),
	BUSINESS("Plano empresarial"),
	ENTERPRISE("Plano Corporativo");
	
	
	private final String descricao;
	
	private TipoPlano(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

}
