package br.com.cadastroit.services.api.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Past;

import br.com.cadastroit.services.api.enums.TipoPessoa;
import br.com.cadastroit.sevices.web.dto.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PESSOA")
@SequenceGenerator(name = "PESSOA_SEQ", sequenceName = "PESSOA_SEQ", allocationSize = 1, initialValue = 1)
public class Pessoa extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 8216819150447358194L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PESSOA_SEQ")
	@Column(name = "ID", nullable = false)
	private Long id;

	@Column(name = "NOME")
	private String nome;

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATANASCIMENTO")
	private Date dataNascimento;

	@Enumerated(EnumType.STRING)
	@Column(name = "TIPOPESSOA")
	private TipoPessoa tipoPessoa;

	@ManyToOne(fetch = FetchType.EAGER) 
	@JoinColumn(name = "ENDERECO_ID", referencedColumnName = "ID", nullable = false)
	private Endereco endereco;

	@Valid
	@OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Juridica juridica;

	@Valid
	@OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Fisica fisica;
	
    // @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // private List<Conta> contas = new ArrayList<Conta>();

}