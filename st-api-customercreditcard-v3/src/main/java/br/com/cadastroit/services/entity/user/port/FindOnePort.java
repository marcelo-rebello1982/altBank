package br.com.cadastroit.services.entity.user.port;

public interface FindOnePort<IN, OUT> {

    OUT execute(IN in);
}