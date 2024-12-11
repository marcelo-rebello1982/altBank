package br.com.cadastroit.services.security.exceptions.handler;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.cadastroit.services.security.exceptions.ErrorResponse;
import br.com.cadastroit.services.security.exceptions.MessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class MainExceptionHandler {

	@ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(HttpServletRequest req, Exception e) {
    	e.printStackTrace();
    	log.info(e.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(new ErrorResponse(500, req.getServletPath(), e.getMessage(), LocalDateTime.now()));
    }
	
	@ExceptionHandler(AuthorizationDeniedException.class)
	ResponseEntity<MessageResponse> handleAuthorizationDeniedException(HttpServletRequest req, AuthorizationDeniedException exception) {
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
				
				MessageResponse.builder()
							.statusCode(HttpStatus.UNAUTHORIZED.value())
							.message("Acesso negado")
							.path(req.getServletPath())
							.timestamp(LocalDateTime.now())
							.build());
		
	}
}