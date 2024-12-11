package br.com.cadastroit.services.entity.product.port;
import org.springframework.data.domain.Page;

public interface SearchAllPageProductPort<PAGE, SIZE, IN, OUT> {
	
	public Page<OUT> execute(PAGE page, SIZE size, IN in);

}