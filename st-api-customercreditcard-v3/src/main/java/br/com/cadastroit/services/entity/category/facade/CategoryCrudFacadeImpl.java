package br.com.cadastroit.services.entity.category.facade;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.cadastroit.services.entity.category.dto.CategoryResponseDTO;
import br.com.cadastroit.services.entity.category.port.GetAllCategoriesPort;


@Component
public class CategoryCrudFacadeImpl implements CategoryCrudFacade {

	private final GetAllCategoriesPort<CategoryResponseDTO> getAllCategoriesPort;

	public CategoryCrudFacadeImpl(GetAllCategoriesPort<CategoryResponseDTO> getAllCategoriesPort) {
		this.getAllCategoriesPort = getAllCategoriesPort;
	}

	@Override
	public List<CategoryResponseDTO> findAll() {
		return this.getAllCategoriesPort.execute();
	}

}