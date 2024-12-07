package br.com.cadastroit.services.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
@Table(name = "CONTA")
@SequenceGenerator(name = "CONTA_SEQ", sequenceName = "CONTA_SEQ", allocationSize = 1, initialValue = 1)
public class Conta extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 2867249081492068332L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CONTA_SEQ")
	@Column(name = "ID", nullable = false)
	private Long id;

	@Column(name = "AGENCIA")
    private int agencia;
	
	@Column(name = "NUMERO")
    private int numero;
	
	@Column(name = "SALDO")
    private BigDecimal saldo;
	
	@Column(name = "LIMITECREDITO")
    private BigDecimal limiteCredito;
	
	@Builder.Default
    @Column(name = "INATIVA")
    private Boolean inativada = false;
	
	@ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "PESSOA_ID")
    private Pessoa pessoa;

}