package br.com.cadastroit.services.repositories.impl;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.cadastroit.services.api.domain.Pessoa;

public interface PessoaRepositoryImpl extends PagingAndSortingRepository<Pessoa, Long> {}

