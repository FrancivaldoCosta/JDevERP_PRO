package br.com.jdeverp.pro.dto;

import lombok.Data;

/*DTO (ou Record) - Data Transfer Object = Objeto de Transferência de dados.*/
@Data
public class UsuarioDto {
	
	private Long id;
	private String pessoa;
	private Boolean liberado;
	private String empresa;
	private String tipoClienteFuncionario;

}
