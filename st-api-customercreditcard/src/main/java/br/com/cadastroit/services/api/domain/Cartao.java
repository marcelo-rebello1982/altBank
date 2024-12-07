package br.com.cadastroit.services.api.domain;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;

import br.com.cadastroit.services.api.enums.CartaoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "CARTAO")
@SequenceGenerator(name = "CARTAO_SEQ", sequenceName = "CARTAO_SEQ", allocationSize = 1, initialValue = 1)
public class Cartao implements Serializable {

	private static final long serialVersionUID = 2141771102732866528L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CARTAO_SEQ")
    @Column(name = "ID")
    private Long id;
	
    @Column(name = "NUMEROCARTAO")
    private String numeroCartao;
   
    @Column(name = "BANDEIRA")
    private String bandeira;

    @DecimalMin("10.00")
    @Column(name = "LIMITECREDITO")
    private BigDecimal limiteCredito;

    @Column(name = "NOMETITULAR")
    private String nomeTitular;

    @Column(name = "CODIGOSEGURANCA")
    private BigDecimal codigoSeguranca;
    
    @Column(name = "ATIVO")
    private Boolean ativo;
    
    @Column(name = "BLOQUEADO")
    private Boolean bloqueado;
    
    @Column(name = "ENTREGUEVALIDADO")
    private Boolean entregueValidado;
    
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CartaoStatus status;
    
    @ManyToOne
    @JoinColumn(name = "PESSOA_ID")
    private Pessoa pessoa;
    
    @ManyToOne
    @JoinColumn(name = "CONTA_ID")
    private Conta conta;
    
    @ManyToMany(mappedBy = "pessoa", fetch = FetchType.EAGER)
	private List<CartaoPessoa> cartoesPessoa;
    
}
