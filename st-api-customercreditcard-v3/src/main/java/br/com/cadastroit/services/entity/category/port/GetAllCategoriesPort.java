package br.com.cadastroit.services.entity.category.port;
import java.util.List;

public interface GetAllCategoriesPort<OUT> {

	List<OUT> execute();
}