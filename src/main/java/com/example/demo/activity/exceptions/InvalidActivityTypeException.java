package com.example.demo.activity.exceptions;

public class InvalidActivityTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6612567622782066254L;
	
	public InvalidActivityTypeException() {
		super();
	}

	public InvalidActivityTypeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidActivityTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidActivityTypeException(String message) {
		super(message);
	}

	public InvalidActivityTypeException(Throwable cause) {
		super(cause);
	}

	

	
}
