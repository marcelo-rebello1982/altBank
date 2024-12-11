package br.com.cadastroit.services.security.facade;

import br.com.cadastroit.services.entity.user.dto.UserRequesLogintDTO;

public interface SecurityFacade {

	String login(UserRequesLogintDTO userRequesLogintDTO);

}