package com.example.demo.classes;

public class User {
	protected enum types{PLAYER, MANAGER};
	private String email;

	public User() {
		super();
	}

	public User(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
