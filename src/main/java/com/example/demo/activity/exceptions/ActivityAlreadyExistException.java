package com.example.demo.activity.exceptions;

public class ActivityAlreadyExistException extends Exception{

	private static final long serialVersionUID = 7068231265511556011L;

	public ActivityAlreadyExistException() {
		super();
	}

	public ActivityAlreadyExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ActivityAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActivityAlreadyExistException(String message) {
		super(message);
	}

	public ActivityAlreadyExistException(Throwable cause) {
		super(cause);
	}

}
