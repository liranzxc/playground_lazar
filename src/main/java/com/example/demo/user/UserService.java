package com.example.demo.user;

import com.example.demo.user.exceptions.EmailAlreadyRegisteredException;
import com.example.demo.user.exceptions.UserNotFoundException;

public interface UserService {

	public void registerNewUser(UserEntity user) throws EmailAlreadyRegisteredException;
	public void updateUserInfo(UserEntity user) throws UserNotFoundException;
	public UserEntity getUser(String email) throws UserNotFoundException;
	//public void deleteUser(String email) throws UserNotFoundException; //Currently not needed
	//public List<UserEntity> getAllUsers(int size, int page) throws InvalidPageSizeRequestException, InvalidPageRequestException;
	//public List<UserEntity> getAllDeletedUsers(); //Currently not needed
	public void cleanup();
	
}
