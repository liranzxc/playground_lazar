package com.example.demo.user;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.Document;



@Entity
@Document
//@Table(name = "USERS")
public class UserEntity {
	private static int ID = 0;

	private String email;
	
	private String playground;

	private String username;
	private String avatar;
	private String role;
	private Long points = 0l; // new user always starts with 0 points
	private String id;
	private String code;
	
	public UserEntity(String email, String playground, String username, String avatar, String role) {
		super();
		this.id = String.valueOf(ID++);
		this.email = email;
		this.username = username;
		this.avatar = avatar;
		this.role = role;
	}
	//Constructor which get a NewUserForm and creates a new user from the details.

	public UserEntity() {
		super();
		this.id = String.valueOf(ID++);
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
	
	public void addPoints(Long points) {
		this.points += points;
	}
	
	public String getId() {
		return this.id;
	}
	
	//empty setter for hiberanate, id should not be able to be change from the outside
	public void setId(String id) {

	}
	
	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}

}
