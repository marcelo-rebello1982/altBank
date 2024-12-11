package br.com.cadastroit.services.security.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDTO {
	private String token;
}