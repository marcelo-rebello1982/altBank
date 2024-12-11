package br.com.cadastroit.services.entity.user.port;

import org.springframework.data.domain.Page;

public interface GetByFiltersPort<IN, IN1, OUT>{

    Page<OUT> execute(IN in, IN1 in1);
}