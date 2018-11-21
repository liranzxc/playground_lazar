package com.example.demo.classes;

public class UnvalidEmailException extends Exception {
	
	private static final long serialVersionUID = -4388733646327053536L;
	
	
	public UnvalidEmailException() {
		super();
	}

	public UnvalidEmailException(String message, Throwable cause) {
		super(message, cause);	
	}

	public UnvalidEmailException(String message) {
		super(message);
	}

	public UnvalidEmailException(Throwable cause) {
		super(cause);
	}

}
