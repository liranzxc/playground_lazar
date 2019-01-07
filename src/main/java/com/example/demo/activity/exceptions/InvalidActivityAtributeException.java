package com.example.demo.activity.exceptions;

public class InvalidActivityAtributeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6369470632010630884L;
	
	public InvalidActivityAtributeException() {
		super();
	}

	public InvalidActivityAtributeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidActivityAtributeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidActivityAtributeException(String message) {
		super(message);
	}

	public InvalidActivityAtributeException(Throwable cause) {
		super(cause);
	}

	

}
