package com.example.demo.activity.exceptions;

public class InvalidEggSizeException extends RuntimeException {

	private static final long serialVersionUID = -8811684445401041623L;

	public InvalidEggSizeException() {
		super();
	}

	public InvalidEggSizeException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public InvalidEggSizeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidEggSizeException(String arg0) {
		super(arg0);
	}

	public InvalidEggSizeException(Throwable arg0) {
		super(arg0);
	}
	
	
}
