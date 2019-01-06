package com.example.demo.user.exceptions;

public class InvalidEmailException extends Exception {
	
	private static final long serialVersionUID = -4388733646327053536L;
	
	
	public InvalidEmailException() {
		super();
	}

	public InvalidEmailException(String message, Throwable cause) {
		super(message, cause);	
	}

	public InvalidEmailException(String message) {
		super(message);
	}

	public InvalidEmailException(Throwable cause) {
		super(cause);
	}

}
