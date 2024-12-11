package br.com.cadastroit.services.entity.category.dto;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CategoryRequestDTO {
	
	private Long id;
	private String name;
}