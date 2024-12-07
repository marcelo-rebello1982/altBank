package br.com.cadastroit.services.repositories.impl;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.cadastroit.services.api.domain.Conta;

public interface ContaRepositoryImpl extends PagingAndSortingRepository<Conta, Long> {}

