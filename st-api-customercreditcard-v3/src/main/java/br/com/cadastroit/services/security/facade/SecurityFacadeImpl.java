package br.com.cadastroit.services.security.facade;
import org.springframework.stereotype.Component;

import br.com.cadastroit.services.entity.user.dto.UserRequesLogintDTO;
import br.com.cadastroit.services.security.port.LoginPort;

@Component
public class SecurityFacadeImpl implements SecurityFacade {

	private final LoginPort<UserRequesLogintDTO, String> loginPort;

	public SecurityFacadeImpl(LoginPort<UserRequesLogintDTO, String> loginPort) {
		this.loginPort = loginPort;
	}

	@Override
	public String login(UserRequesLogintDTO userRequesLogintDTO) {
		return this.loginPort.execute(userRequesLogintDTO);
	}

}