package com.example.demo.element.exceptions;

public class InvalidElementForActivityException extends Exception {

	private static final long serialVersionUID = -3342214321727992658L;

	public InvalidElementForActivityException() {
		super();
	}

	public InvalidElementForActivityException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public InvalidElementForActivityException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidElementForActivityException(String arg0) {
		super(arg0);
	}

	public InvalidElementForActivityException(Throwable arg0) {
		super(arg0);
	}

}
