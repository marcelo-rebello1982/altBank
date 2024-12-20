package br.com.cadastroit.services.entity.product.dto;
import java.math.BigDecimal;

import br.com.cadastroit.services.entity.category.dto.CategoryRequestDTO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductRequestDTO {
	
	private Long id;
	private String name;
	private String description;
	private CategoryRequestDTO categoryPath;
	private BigDecimal price;
	private Boolean available;

}