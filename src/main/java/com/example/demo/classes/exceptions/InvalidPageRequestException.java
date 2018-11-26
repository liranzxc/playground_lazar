package com.example.demo.classes.exceptions;

public class InvalidPageRequestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String defaultMessage = "Page must be at least 0";

	public InvalidPageRequestException(String string) {
		super(string);
	}

	public InvalidPageRequestException() {
		super(defaultMessage);
	}

}
