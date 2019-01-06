package com.example.demo.user;

import org.springframework.stereotype.Component;

import com.example.demo.aop.EmailValue;
import com.example.demo.aop.UserPermission;
import com.example.demo.aop.UserExistInDB;

@Component
public class UserVerifyier {

	public UserVerifyier() {
		super();
	}
	
	@UserExistInDB
	public void verify(@EmailValue String email) {}
	
	
	@UserPermission
	public String getType(@EmailValue String email) {
		return email;
	}
	
	
	
}
