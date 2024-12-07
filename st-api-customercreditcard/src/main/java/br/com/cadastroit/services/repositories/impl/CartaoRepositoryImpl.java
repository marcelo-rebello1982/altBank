package br.com.cadastroit.services.repositories.impl;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.cadastroit.services.api.domain.Cartao;

public interface CartaoRepositoryImpl extends PagingAndSortingRepository<Cartao, Long> {}

