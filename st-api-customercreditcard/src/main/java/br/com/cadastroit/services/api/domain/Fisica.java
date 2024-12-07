package br.com.cadastroit.services.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.br.CPF;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FISICA")
@SequenceGenerator(name = "FISICA_SEQ", sequenceName = "FISICA_SEQ", allocationSize = 1, initialValue = 1)
public class Fisica implements Serializable {
	
	private static final long serialVersionUID = -8751414189080493217L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "FISICA_SEQ")
	@Column(name = "ID", nullable = false)
	private Long id;
	
	@CPF
	@Column(name = "NUM_CPF", nullable = false)
	private BigDecimal numCpf;
	
	@Column(name = "RG", nullable = true)
	private String rg;
	
	@OneToOne
	@JoinColumn(name = "PESSOA_ID")
	private Pessoa pessoa;
	
}