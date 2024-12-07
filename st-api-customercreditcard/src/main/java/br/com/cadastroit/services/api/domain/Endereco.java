package br.com.cadastroit.services.api.domain;
import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.cadastroit.services.api.enums.TipoEndereco;
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
@Table(name = "ENDERECO")
@SequenceGenerator(name = "ENDERECO_SEQ", sequenceName = "ENDERECO_SEQ", allocationSize = 1)
public class Endereco extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 4451252181049601853L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENDERECO_SEQ")
    @Column(name = "ID", nullable = false)
	private Long id;

	@Column(name = "NUMERO", nullable = false)
	private String numero;

	private String complemento;

	@Column(name = "BAIRRO", nullable = false)
	private String bairro;
	
	@OneToMany(mappedBy = "endereco", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Pessoa> pessoas;

	@Enumerated(EnumType.STRING)
	private TipoEndereco tipoEndereco;

	@Enumerated(EnumType.STRING)
	private Logradouro logradouro;
	
}
