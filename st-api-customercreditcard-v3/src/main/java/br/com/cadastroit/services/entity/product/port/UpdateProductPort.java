package br.com.cadastroit.services.entity.product.port;
public interface UpdateProductPort<IN, OUT> {
	
	OUT execute(IN in);

}