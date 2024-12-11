package br.com.cadastroit.services.security.port;

public interface ValidateTokenPort<IN, OUT> {

    OUT execute(IN in);
}