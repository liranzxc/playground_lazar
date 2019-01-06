package com.example.demo.user.exceptions;

public class InvalidConfirmationCodeException extends Exception{

	private static final long serialVersionUID = -7070975769200487298L;
	
	public InvalidConfirmationCodeException() {
		super();
	}


	public InvalidConfirmationCodeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidConfirmationCodeException(String arg0) {
		super(arg0);
	}

	public InvalidConfirmationCodeException(Throwable arg0) {
		super(arg0);
	}

}
