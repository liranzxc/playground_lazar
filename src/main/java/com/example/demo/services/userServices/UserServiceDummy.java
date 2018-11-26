package com.example.demo.services.userServices;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.example.demo.classes.EntityClasses.UserEntity;
import com.example.demo.classes.exceptions.InvalidEmailException;
import com.example.demo.classes.exceptions.UserNotFoundException;

//@Service
public class UserServiceDummy implements IUserService{

	private Map<String, UserEntity> allRegisteredUsers = new ConcurrentHashMap<>();;
	private Map<String, UserEntity> deletedUsers = new ConcurrentHashMap<>();
	
	
	
	@Override
	public void registerNewUser(UserEntity user){ //need to create new exception for already registered email
		allRegisteredUsers.put(user.getEmail(), user);
	}

	@Override
	public void updateUserInfo(UserEntity user) {
		allRegisteredUsers.put(user.getEmail(), user); //assume not null in important fields.
	}

	@Override
	public UserEntity getUser(String email) throws UserNotFoundException {
		if(this.allRegisteredUsers.containsKey(email)) {
			return allRegisteredUsers.get(email);
		}
		else {
			throw new UserNotFoundException(); 
		}
	}

	@Override
	public void deleteUser(String email) throws UserNotFoundException {
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
