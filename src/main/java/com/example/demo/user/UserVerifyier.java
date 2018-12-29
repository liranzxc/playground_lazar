package com.example.demo.user;

import org.springframework.stereotype.Component;

import com.example.demo.aop.EmailValue;
import com.example.demo.aop.PermissionLog;
import com.example.demo.aop.UserExistInDB;

@Component
public class UserVerifyier {

	public UserVerifyier() {
		super();
	}
	
	@UserExistInDB
	public void verify(@EmailValue String email) {}
	
	
	@PermissionLog
	public String getType(@EmailValue String email) {
		return email;
	}
	
	
	
}
