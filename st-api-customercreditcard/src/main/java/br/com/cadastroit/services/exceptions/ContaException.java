package br.com.cadastroit.services.exceptions;

public class ContaException extends GenericException {

	private static final long serialVersionUID = -3388004643431687498L;

	public ContaException(String message) {
		super(message);
	}

	public ContaException(String message, Throwable cause) {
		super(buildMessage(message, cause), cause);
	}
}

