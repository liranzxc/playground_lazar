package com.example.demo.application.exceptions;

public class InvalidPageSizeRequestException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String defaultMessage = "Page size must be at least 1";

	public InvalidPageSizeRequestException(String string) {
		super(string);
	}

	public InvalidPageSizeRequestException() {
		super(defaultMessage);
	}

}
