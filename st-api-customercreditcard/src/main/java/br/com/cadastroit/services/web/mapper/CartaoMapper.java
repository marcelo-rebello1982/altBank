package br.com.cadastroit.services.web.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import br.com.cadastroit.services.api.domain.Cartao;
import br.com.cadastroit.sevices.web.dto.CartaoDTO;
import br.com.cadastroit.sevices.web.dto.CartaoResumeDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CartaoMapper {

	public abstract CartaoDTO toDTO(Cartao entity);

	public abstract List<CartaoDTO> toDTO(List<Cartao> entity);
	
	public abstract Cartao toEntity(CartaoDTO dto);
	
	@Mappings({ @Mapping(target = "numeroCartao", source = "entity.numeroCartao"),
		@Mapping(target = "bandeira", source = "entity.bandeira"),
		@Mapping(target = "entregueValidado", source = "entity.entregueValidado"),
		@Mapping(target = "status", source = "entity.status") })
	public abstract CartaoResumeDTO toResumeDTO(Cartao entity);
	
	public abstract List<CartaoResumeDTO> toResumeDTO(List<Cartao> entity);
	
}
