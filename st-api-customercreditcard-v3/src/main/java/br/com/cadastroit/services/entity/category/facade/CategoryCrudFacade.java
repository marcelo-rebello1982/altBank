package br.com.cadastroit.services.entity.category.facade;
import java.util.List;

import br.com.cadastroit.services.entity.category.dto.CategoryResponseDTO;


public interface CategoryCrudFacade {

	List<CategoryResponseDTO> findAll();

}