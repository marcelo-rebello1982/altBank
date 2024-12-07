package br.com.cadastroit.services.web.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import br.com.cadastroit.services.api.domain.Pessoa;
import br.com.cadastroit.sevices.web.dto.PessoaDTO;
import br.com.cadastroit.sevices.web.dto.PessoaResumeDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PessoaMapper {

	public abstract PessoaDTO toDTO(Pessoa entity);

	public abstract List<PessoaDTO> toDTO(List<Pessoa> entity);

	@Mappings({ @Mapping(target = "nome", source = "entity.nome"),
		@Mapping(target = "dataNascimento", source = "entity.dataNascimento") })
	public abstract List<PessoaResumeDTO> toResumeDto(List<Pessoa> entity);
	
	public abstract Pessoa toEntity(PessoaDTO dto);
	
}
