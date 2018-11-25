package com.example.demo.classes.ToClasses;

import org.springframework.stereotype.Component;

import com.example.demo.classes.EntityClasses.UserEntity;

@Component
public class UserTO {

	private String username;
	private String email;
	private String avatar;
	private String role;
	private String playground;
	
	public UserTO(String username, String email, String avatar, String role) {
		this.username = username;
		this.email = email;
		this.avatar = avatar;
		this.role = role;
		this.playground = "playground_lazar";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getPlayground() {
		return playground;
	}

	public void setPlayground(String playground) {
		this.playground = playground;
	}

	
	public UserTO() {
		super();
	}

	public UserTO(UserEntity userEntity) {
		this.avatar = userEntity.getAvatar();
		this.email = userEntity.getEmail();
		this.playground = userEntity.getPlayground();
		this.role = userEntity.getRole();
		this.username = userEntity.getUsername();
	}

	// to Entity object 
	// new method !! Liran Nachman
	public UserEntity ToEntity()
	{
		return new UserEntity(this.email, this.playground
				, this.username, this.avatar, role);
	}
	
}
