package br.com.cadastroit.services.repositories.impl;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.cadastroit.services.api.domain.Endereco;

public interface EnderecoRepositoryImpl extends PagingAndSortingRepository<Endereco, Long> {}

