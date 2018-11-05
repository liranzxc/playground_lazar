package com.example.demo;

public class User {
	protected enum types{PLAYER, MANAGER};
	private Email email;

	public User() {
		super();
	}

	public User(Email email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email.getEmailAsString();
	}

	public void setEmail(Email email) {
		this.email = email;
	}
}
