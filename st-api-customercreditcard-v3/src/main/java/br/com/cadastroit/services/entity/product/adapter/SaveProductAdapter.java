package br.com.cadastroit.services.entity.product.adapter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.cadastroit.services.entity.product.dto.ProductRequestDTO;
import br.com.cadastroit.services.entity.product.dto.ProductResponseDTO;
import br.com.cadastroit.services.entity.product.model.ProductModel;
import br.com.cadastroit.services.entity.product.port.SaveProductPort;
import br.com.cadastroit.services.entity.product.repository.ProductModelRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SaveProductAdapter implements SaveProductPort<ProductRequestDTO, ProductResponseDTO> {

	private final ProductModelRepository repository;

	private final ModelMapper mapper;

	@Override
	public ProductResponseDTO execute(ProductRequestDTO in) {
		ProductModel model = mapper.map(in, ProductModel.class);
		model = repository.save(model);
		return mapper.map(model, ProductResponseDTO.class);
	}

}