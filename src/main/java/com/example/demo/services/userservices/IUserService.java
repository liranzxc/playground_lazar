package com.example.demo.services.userservices;

import java.util.List;

import com.example.demo.classes.entities.UserEntity;
import com.example.demo.classes.exceptions.EmailAlreadyRegisteredException;
import com.example.demo.classes.exceptions.InvalidEmailException;
import com.example.demo.classes.exceptions.InvalidPageRequestException;
import com.example.demo.classes.exceptions.InvalidPageSizeRequestException;
import com.example.demo.classes.exceptions.UserNotFoundException;

public interface IUserService {

	public void registerNewUser(UserEntity user) throws EmailAlreadyRegisteredException;
	public void updateUserInfo(UserEntity user) throws UserNotFoundException;
	public UserEntity getUser(String email) throws UserNotFoundException;
	//public void deleteUser(String email) throws UserNotFoundException; //Currently not needed
	//public List<UserEntity> getAllUsers(int size, int page) throws InvalidPageSizeRequestException, InvalidPageRequestException;
	//public List<UserEntity> getAllDeletedUsers(); //Currently not needed
	public void cleanup();
	
}
