package br.com.cadastroit.services.exceptions;

public class PessoaException extends GenericException {

	private static final long serialVersionUID = 495716847873446465L;

	public PessoaException(String message) {
		super(message);
	}

	public PessoaException(String message, Throwable cause) {
		super(buildMessage(message, cause), cause);
	}
}

