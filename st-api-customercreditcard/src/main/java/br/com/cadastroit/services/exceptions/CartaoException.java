package br.com.cadastroit.services.exceptions;

public class CartaoException extends GenericException {

	private static final long serialVersionUID = 6039003299234982862L;

	public CartaoException(String message) {
		super(message);
	}

	public CartaoException(String message, Throwable cause) {
		super(buildMessage(message, cause), cause);
	}
}

