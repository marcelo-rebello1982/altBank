package br.com.cadastroit.services.security.port;

public interface LoginPort<IN ,OUT> {
    OUT execute(IN in);
}