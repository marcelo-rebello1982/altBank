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

import org.hibernate.validator.constraints.br.CNPJ;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "JURIDICA")
@SequenceGenerator(name = "JURIDICA_SEQ", sequenceName = "JURIDICA_SEQ", allocationSize = 1, initialValue = 1)
public class Juridica implements Serializable {
	
	private static final long serialVersionUID = 7224646269176340411L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "JURIDICA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@CNPJ
	@Column(name = "NUM_CNPJ")
	private BigDecimal numCnpj;
	
	@OneToOne
	@JoinColumn(name = "PESSOA_ID")
	private Pessoa pessoa;
	
}