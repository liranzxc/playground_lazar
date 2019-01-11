package com.example.demo.user;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.example.demo.user.exceptions.UserNotFoundException;

//@Service
public class UserServiceDummy implements UserService{

	private Map<String, UserEntity> allRegisteredUsers = new ConcurrentHashMap<>();;
	
	@Override
	public void registerNewUser(UserEntity user){ 
		allRegisteredUsers.put(user.getEmail(), user);
	}

	@Override
	public void updateUserInfo(UserEntity user) {
		allRegisteredUsers.put(user.getEmail(), user); //assume not null in important fields.
	}

	@Override
	public UserEntity getUser(String email, String playground) throws UserNotFoundException {
		if(this.allRegisteredUsers.containsKey(email)) {
			return allRegisteredUsers.get(email);
		}
		else {
			throw new UserNotFoundException(); 
		}
	}
	

	@Override
	public void cleanup() {
		allRegisteredUsers.clear();
	}
}
