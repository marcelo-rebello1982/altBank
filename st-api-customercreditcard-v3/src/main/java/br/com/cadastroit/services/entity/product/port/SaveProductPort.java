package br.com.cadastroit.services.entity.product.port;
public interface SaveProductPort<IN, OUT> {

	OUT execute(IN in);

}