package br.com.cadastroit.services.security.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cadastroit.services.entity.user.dto.UserRequesLogintDTO;
import br.com.cadastroit.services.security.dto.TokenDTO;
import br.com.cadastroit.services.security.facade.SecurityFacade;

@RestController
@RequestMapping("api/v1/security")
public class SecurityController {

	private final SecurityFacade securityFacade;

	public SecurityController(SecurityFacade securityFacade) {
		this.securityFacade = securityFacade;
	}

	@PostMapping("login")
	public ResponseEntity<TokenDTO> login(@RequestBody UserRequesLogintDTO userRequesLogintDTO) {
		String token = this.securityFacade.login(userRequesLogintDTO);
		return new ResponseEntity<>(TokenDTO.builder().token(token).build(), HttpStatus.OK);
	}

}