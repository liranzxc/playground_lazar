package com.example.demo.element.exceptions;

public class InvalidElementForActivityException extends Exception {
	
	private static final long serialVersionUID = 8141458427641371485L;

	public InvalidElementForActivityException() {
		super();
	}

	public InvalidElementForActivityException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidElementForActivityException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidElementForActivityException(String message) {
		super(message);
	}

	public InvalidElementForActivityException(Throwable cause) {
		super(cause);
	}

	
	
}

