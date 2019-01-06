package com.example.demo.element.exceptions;

public class ElementNotFoundException extends Exception {

	private static final long serialVersionUID = -4317095474928084637L;

	public ElementNotFoundException() {
		super();
	}

	public ElementNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ElementNotFoundException(String message) {
		super(message);
	}

	public ElementNotFoundException(Throwable cause) {
		super(cause);
	}

}
