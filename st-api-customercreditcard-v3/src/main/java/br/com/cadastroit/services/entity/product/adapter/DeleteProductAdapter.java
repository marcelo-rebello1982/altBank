package br.com.cadastroit.services.entity.product.adapter;
import org.springframework.stereotype.Component;

import br.com.cadastroit.services.entity.product.port.DeleteProductPort;
import br.com.cadastroit.services.entity.product.repository.ProductModelRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DeleteProductAdapter implements DeleteProductPort<Long> {

	private final ProductModelRepository repository;

	@Override
	public void execute(Long in) {
		repository.deleteById(in);
	}

}