package com.example.demo.classes.exceptions;

public class UserNotActivatedException extends Exception{



	private static final long serialVersionUID = 885220268666834512L;



	public UserNotActivatedException() {
		super();
	}


	public UserNotActivatedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UserNotActivatedException(String arg0) {
		super(arg0);
	}

	public UserNotActivatedException(Throwable arg0) {
		super(arg0);
	}

	
}
