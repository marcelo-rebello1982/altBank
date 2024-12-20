package br.com.cadastroit.services.entity.product.dto;
import java.math.BigDecimal;

import br.com.cadastroit.services.entity.category.dto.CategoryResponseDTO;
import lombok.Data;

@Data
public class ProductResponseDTO {
	private Long id;
	private String name;
	private String description;
	private CategoryResponseDTO categoryPath;
	private BigDecimal price;
	private Boolean available;
}