package com.example.demo.services;

import java.util.List;

import com.example.demo.classes.EntityClasses.UserEntity;

public interface IUserService {

	public void registerNewUser(UserEntity user) throws Exception;
	public void updateUserInfo(UserEntity user);
	public UserEntity getUser(String email) throws Exception;
	public void deleteUser(String email) throws Exception;
	public List<UserEntity> getAllUsers();
	public List<UserEntity> getAllDeletedUsers();
	public void cleanup();
	
}
