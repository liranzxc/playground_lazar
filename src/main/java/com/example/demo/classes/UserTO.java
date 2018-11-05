package com.example.demo.classes;

public class UserTO {
	
	private String email;
	private String playground;
	private String username;
	private String avatar;
	private String role;
	private Long points = 0l; // new user always starts with 0 points
	public UserTO(String email, String playground, String username, String avatar, String role, Long points) {
		super();
		this.email = email;
		this.playground = playground;
		this.username = username;
		this.avatar = avatar;
		this.role = role;
		this.points = points;
	}
	//Constructor which get a NewUserForm and creates a new user from the details.
	public UserTO(NewUserForm userForm) {
		super();
		this.setUsername(userForm.getUsername());
		this.setPlayground(playground);
		this.setEmail(userForm.getEmail());
		this.setAvatar(userForm.getAvatar());
		this.setRole(userForm.getRole());
	}
	
	public UserTO() {
		super();
	}
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
