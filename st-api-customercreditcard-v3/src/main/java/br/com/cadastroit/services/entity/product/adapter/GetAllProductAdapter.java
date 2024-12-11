package br.com.cadastroit.services.entity.product.adapter;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.cadastroit.services.entity.product.dto.ProductResponseDTO;
import br.com.cadastroit.services.entity.product.port.GetAllProductPort;
import br.com.cadastroit.services.entity.product.repository.ProductModelRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class GetAllProductAdapter implements GetAllProductPort<ProductResponseDTO> {

	private final ProductModelRepository repository;

	private final ModelMapper mapper;

	@Override
	public List<ProductResponseDTO> execute() {
		List<ProductResponseDTO> list = this.repository.findAll().stream()
				.map(product -> this.mapper.map(product, ProductResponseDTO.class)).collect(Collectors.toList());
		return list;
	}

}