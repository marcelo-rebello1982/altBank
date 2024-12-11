package br.com.cadastroit.services.entity.user.port;

public interface SavePort<IN, OUT> {

    OUT execute(IN in);
}