package com.example.demo.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.demo.classes.EntityClasses.UserEntity;

public class UserServiceDummy implements IUserService{

	private Map<String, UserEntity> allRegisteredUsers;
	private Map<String, UserEntity> deletedUsers;

	
	@Override
	public void registerNewUser(UserEntity user) throws Exception {
		allRegisteredUsers.put(user.getEmail(), user);
		
	}

	@Override
	public void updateUserInfo(UserEntity user) {
		allRegisteredUsers.put(user.getEmail(), user); //asserting not null in important fields.
	}

	@Override
	public UserEntity getUser(String email) throws Exception {
		return allRegisteredUsers.get(email);
	}

	@Override
	public void deleteUser(String email) throws Exception {
		UserEntity user = allRegisteredUsers.remove(email);
		deletedUsers.put(user.getEmail(), user);
		
	}

	@Override
	public List<UserEntity> getAllUsers() {
		return allRegisteredUsers.values().stream().collect(Collectors.toList());
	}

	@Override
	public List<UserEntity> getAllDeletedUsers() {
		return deletedUsers.values().stream().collect(Collectors.toList());
	}

	@Override
	public void cleanup() {
		allRegisteredUsers.clear();
	}

}
