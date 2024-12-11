package br.com.cadastroit.services.security.exceptions;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageResponse {
	
	private int statusCode;
	private String path;
	private String message;
	private LocalDateTime timestamp;
	
}

