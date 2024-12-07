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
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "CARTAOPESSOA")
@SequenceGenerator(name = "CARTAOPESSOA_SEQ", sequenceName = "CARTAOPESSOA_SEQ", allocationSize = 1, initialValue = 1)
public class CartaoPessoa implements Serializable {

	private static final long serialVersionUID = 3378316661478201477L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CARTAOPESSOA_SEQ")
	@Column(name = "ID")
	private Long id;

	@Positive
	@Column(name = "NROPEDIDOCARTAO")
	private Integer nroPedidoCartao;
	
    @DecimalMin("100.00")
    @Column(name = "LIMITE_MINIMO")
    private BigDecimal limiteMinimo;

	@JoinColumn(name = "PESSOA_ID")
	@ManyToOne(fetch = FetchType.EAGER)
	private Pessoa pessoa;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CARTAO_ID")
	private Cartao cartao;
	
}
