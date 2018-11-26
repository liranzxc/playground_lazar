package com.example.demo.services.userServices;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.UserRepository;
import com.example.demo.classes.EntityClasses.UserEntity;
import com.example.demo.classes.exceptions.EmailAlreadyRegisteredException;
import com.example.demo.classes.exceptions.UserNotFoundException;

@Service
public class UserServiceJPA implements IUserService{

	@Autowired
	private UserRepository dataBase;
	
	//TODO id must be fixed
	
	@Override
	public void registerNewUser(UserEntity user) throws EmailAlreadyRegisteredException {
		if (!dataBase.existsById(null)) {
			dataBase.save(user);
		}
		else
			throw new EmailAlreadyRegisteredException("The email address " + user.getEmail() +" is already registered.");
		
	}

	@Override
	public void updateUserInfo(UserEntity user) throws UserNotFoundException {
		if (dataBase.existsById(null)) {
			dataBase.save(user);
		}
		else
			throw new UserNotFoundException("The user " +user.getEmail() +" not found.");
		
	}

	@Override
	public UserEntity getUser(String email) throws UserNotFoundException {
		if (dataBase.existsById(null)) {
			return dataBase.findById(null).get();
		}
		else
			throw new UserNotFoundException("The user " +email +" not found.");
	}

	@Override
	public void deleteUser(String email) throws UserNotFoundException {
		if (dataBase.existsById(null)) {
			dataBase.deleteById(null);
		}
		else
			throw new UserNotFoundException("The user " +email +" not found.");
		
	}

	//TODO create pagination
	@Override
	public List<UserEntity> getAllUsers() {
		List<UserEntity> list = StreamSupport.stream(dataBase.findAll().spliterator(), false)
				.collect(Collectors.toList());
		return list;
	}

	@Override
	public List<UserEntity> getAllDeletedUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cleanup() {
		dataBase.deleteAll();
	}

}
