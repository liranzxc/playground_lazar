package com.example.demo.user;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;



@Entity
@Document
public class UserEntity {
	private final long START_POINTS = 0l;
	private static int ID = 0;

	private String email;
	
	private String playground = "playground_lazar";

	private String username;
	private String avatar;
	private String role;
	private Long points; 
	private String id;
	private String code;
	

	public UserEntity(String email, String playground, String username, String avatar, String role) {
		super();
		this.id = String.valueOf(ID++);
		this.email = email;
		this.username = username;
		this.avatar = avatar;
		this.role = role;
		this.points = START_POINTS;
	}

	public UserEntity() {
		super();
		this.id = String.valueOf(ID++);
		this.points = START_POINTS;
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
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
