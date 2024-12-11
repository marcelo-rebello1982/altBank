package br.com.cadastroit.services.entity.product.port;
import java.util.List;

public interface GetAllProductPort<OUT> {

	public List<OUT> execute();

}