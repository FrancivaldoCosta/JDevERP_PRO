package br.com.jdeverp.pro.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CNPJ.Format;
import org.hibernate.validator.constraints.br.CPF;

import br.com.jdeverp.pro.enums.TipoPessoa;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "pessoa", uniqueConstraints = {
		@UniqueConstraint(name = "unique_inscricao_estadual", columnNames = "inscricao_estadual"),
		@UniqueConstraint(name = "unique_cnpj", columnNames = "cnpj"),
		@UniqueConstraint(name = "unique_cpf", columnNames = "cpf"),
		@UniqueConstraint(name = "unique_email", columnNames = "email"),
})
@SequenceGenerator(name = "seq_pessoa", sequenceName = "seq_pessoa", allocationSize = 1, initialValue = 1)
public class Pessoa implements Serializable {

	private static final Long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pessoa")
	private Long id;

	@NotBlank(message = "Nome deve ser informado")
	@Column(length = 200, nullable = false)
	private String nome;

	@Column(name = "razao_social", length = 200)
	private String razaoSocial;

	@Column(name = "nome_fantasia", length = 200)
	private String nomeFantasia;

	@Column(name = "inscricao_estadual", length = 200, unique = true)
	private String inscricaoEstadual;

	@CNPJ(format = Format.ALPHANUMERIC, message = "Informe o CNPJ corretamente")
	@Column(length = 50, unique = true)
	private String cnpj;

	@NotBlank(message = "Informe o telefone corretamente")
	@Column(length = 50, nullable = false)
	private String telefone;

	@CPF(message = "Informe o CPF corretamente")
	@Column(length = 30, nullable = false, unique = true)
	private String cpf;

	@Email(message = "E-mail deve ser informado corretamente")
	@Column(length = 250, nullable = false, unique = true)
	private String email;

	@NotNull(message = "Tipo da pessoa deve ser informado")
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_pessoa", nullable = false)
	private TipoPessoa tipoPessoa;

	private Boolean ativo;

	@NotNull(message = "Data de cadastro deve ser informada")
	@Column(name = "data_cadastro", nullable = false, updatable = false)
	private LocalDateTime dataCadastro = LocalDateTime.now();

	@Column(length = 1000)
	private String observacao;

	@NotBlank(message = "Informe o CEP corretamente")
	@Column(length = 50, nullable = false)
	private String cep;

	@NotBlank(message = "Informe o nome da rua corretamente")
	@Column(length = 255, nullable = false)
	private String logradouro;

	@NotBlank(message = "Informe o bairro corretamente")
	@Column(length = 250, nullable = false)
	private String bairro;

	@NotBlank(message = "Informe o estado corretamente")
	@Column(length = 200, nullable = false)
	private String estado;

	@NotBlank(message = "Informe a cidade corretamente")
	@Column(length = 300, nullable = false)
	private String cidade;

	@NotBlank(message = "Informe o país corretamente")
	@Column(length = 250, nullable = false)
	private String pais;

	@Column(length = 400)
	private String complemento;

	/* Refere-se ao cadastro da empresa em multitanenti */
	@NotNull(message = "Empresa deve ser informada corretamente")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_fk"))
	private Empresa empresa;

}