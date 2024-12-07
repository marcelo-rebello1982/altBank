package br.com.cadastroit.services.web.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import br.com.cadastroit.services.api.domain.Conta;
import br.com.cadastroit.sevices.web.dto.ContaDTO;
import br.com.cadastroit.sevices.web.dto.ContaDisabledDTO;
import br.com.cadastroit.sevices.web.dto.ContaResumeDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ContaMapper {

	public abstract ContaDTO toDTO(Conta entity);

	public abstract List<ContaDTO> toDTO(List<Conta> entity);
	
	public abstract Conta toEntity(ContaDTO dto);
	
	@Mappings({ @Mapping(target = "agencia", source = "entity.agencia"),
		@Mapping(target = "numero", source = "entity.numero") })
	public abstract ContaResumeDTO toResumeDTO(Conta entity);
	
	public abstract List<ContaResumeDTO> toResumeDTO(List<Conta> entity);
	
	@Mappings({ @Mapping(target = "inativada", source = "entity.inativada") })
	public abstract ContaDisabledDTO toDisabled(Conta entity);

	
}
