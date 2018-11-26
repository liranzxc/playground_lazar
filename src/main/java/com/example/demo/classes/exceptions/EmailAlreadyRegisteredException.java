package com.example.demo.classes.exceptions;

public class EmailAlreadyRegisteredException extends Exception {
	
	private static final long serialVersionUID = -4388733646327053536L;
	
	
	public EmailAlreadyRegisteredException() {
		super();
	}

	public EmailAlreadyRegisteredException(String message, Throwable cause) {
		super(message, cause);	
	}

	public EmailAlreadyRegisteredException(String message) {
		super(message);
	}

	public EmailAlreadyRegisteredException(Throwable cause) {
		super(cause);
	}

}
