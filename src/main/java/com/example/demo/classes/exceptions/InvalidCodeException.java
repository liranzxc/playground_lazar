package com.example.demo.classes.exceptions;

public class InvalidCodeException extends Exception{

	private static final long serialVersionUID = -7070975769200487298L;
	
	public InvalidCodeException() {
		super();
	}


	public InvalidCodeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidCodeException(String arg0) {
		super(arg0);
	}

	public InvalidCodeException(Throwable arg0) {
		super(arg0);
	}

}
