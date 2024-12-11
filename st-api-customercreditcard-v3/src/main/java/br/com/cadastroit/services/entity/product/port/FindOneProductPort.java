package br.com.cadastroit.services.entity.product.port;
public interface FindOneProductPort<IN, OUT> {
	
	OUT execute(IN in);

}