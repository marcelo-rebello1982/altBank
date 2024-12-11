package br.com.cadastroit.services.security.port;

public interface GenerateTokenPort<IN, OUT>{
    OUT execute(IN in);
}