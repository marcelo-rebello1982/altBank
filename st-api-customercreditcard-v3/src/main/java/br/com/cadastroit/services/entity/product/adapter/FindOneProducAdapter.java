package br.com.cadastroit.services.entity.product.adapter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.cadastroit.services.entity.product.dto.ProductResponseDTO;
import br.com.cadastroit.services.entity.product.port.FindOneProductPort;
import br.com.cadastroit.services.entity.product.repository.ProductModelRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class FindOneProducAdapter implements FindOneProductPort<Long, ProductResponseDTO> {

	private final ProductModelRepository repository;

	private final ModelMapper mapper;

	@Override
	public ProductResponseDTO execute(Long in) {
		return mapper.map(repository.findById(in).orElse(null), ProductResponseDTO.class);
	}

}