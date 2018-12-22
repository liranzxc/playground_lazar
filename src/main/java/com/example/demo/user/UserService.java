package com.example.demo.user;

import com.example.demo.user.exceptions.EmailAlreadyRegisteredException;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotFoundException;

public interface UserService {

	public void registerNewUser(UserEntity user) throws EmailAlreadyRegisteredException, InvalidEmailException, InvalidRoleException; // Invalid role might be useless in the futuree.
	public void updateUserInfo(UserEntity user) throws UserNotFoundException, InvalidEmailException;
	public UserEntity getUser(String email, String playground) throws UserNotFoundException;
	//public void deleteUser(String email) throws UserNotFoundException; //Currently not needed
	//public List<UserEntity> getAllUsers(int size, int page) throws InvalidPageSizeRequestException, InvalidPageRequestException;
	//public List<UserEntity> getAllDeletedUsers(); //Currently not needed
	public void cleanup();
	
}
