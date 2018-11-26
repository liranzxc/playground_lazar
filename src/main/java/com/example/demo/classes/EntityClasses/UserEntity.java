package com.example.demo.classes.EntityClasses;

import javax.persistence.Entity;
import javax.persistence.Id;



@Entity
public class UserEntity {
	
	private String email;
	private String playground;
	private String username;
	private String avatar;
	private String role;
	private Long points = 0l; // new user always starts with 0 points
	public UserEntity(String email, String playground, String username, String avatar, String role) {
		super();
		this.email = email;
		this.playground = playground;
		this.username = username;
		this.avatar = avatar;
		this.role = role;
	}
	//Constructor which get a NewUserForm and creates a new user from the details.

	public UserEntity() {
		super();
	}
	@Id
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPlayground() {
		return playground;
	}
	public void setPlayground(String playground) {
		this.playground = playground;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Long getPoints() {
		return points;
	}
	public void setPoints(Long points) {
		this.points = points;
	}

}
