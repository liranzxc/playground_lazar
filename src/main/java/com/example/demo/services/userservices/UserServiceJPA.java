package com.example.demo.services.userservices;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.classes.entities.UserEntity;
import com.example.demo.classes.exceptions.EmailAlreadyRegisteredException;
import com.example.demo.classes.exceptions.InvalidPageRequestException;
import com.example.demo.classes.exceptions.InvalidPageSizeRequestException;
import com.example.demo.classes.exceptions.UserNotFoundException;
import com.example.demo.repository.IUserRepository;

@Service
public class UserServiceJPA implements IUserService{

	@Autowired
	private IUserRepository dataBase;
	
	//TODO id must be fixed
	
	@Override
	@Transactional
	public void registerNewUser(UserEntity user) throws EmailAlreadyRegisteredException {
		if (!dataBase.existsById(user.getId())) {
			dataBase.save(user);
		}
		else
			throw new EmailAlreadyRegisteredException("The email address " + user.getEmail() +" is already registered.");
		
	}

	@Override
	@Transactional
	public void updateUserInfo(UserEntity user) throws UserNotFoundException {
		if (dataBase.existsById(user.getId())) {
			dataBase.save(user);
		}
		else
			throw new UserNotFoundException("The user " +user.getEmail() +" not found.");
		
	}

	@Override
	@Transactional(readOnly=true)
	public UserEntity getUser(String email) throws UserNotFoundException {
		if (dataBase.existsById(null)) {
			return dataBase.findById(null).get();
		}
		else
			throw new UserNotFoundException("The user " +email +" not found.");
	}

	//Not needed currently
//	@Override
//	@Transactional
//	public void deleteUser(String email) throws UserNotFoundException {
//		if (dataBase.existsById(null)) {
//			dataBase.deleteById(null);
//		}
//		else
//			throw new UserNotFoundException("The user " +email +" not found.");
//		
//	}

	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> getAllUsers(int size, int page) throws InvalidPageSizeRequestException, InvalidPageRequestException {
		if(size < 1)
			throw new InvalidPageSizeRequestException();
		if(page < 0)
			throw new InvalidPageRequestException();
		
		List<UserEntity> list = StreamSupport.stream(dataBase.findAll().spliterator(), false)
				.skip(size * page)
				.limit(size)
				.collect(Collectors.toList());
		return list;
	}

//	@Override
//	@Transactional
//	public List<UserEntity> getAllDeletedUsers() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	@Transactional
	public void cleanup() {
		dataBase.deleteAll();
	}
	

}
