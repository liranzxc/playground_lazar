package com.example.demo.user;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.aop.MyLog;
import com.example.demo.aop.PermisionLog;
import com.example.demo.application.accessories.GeneratorService;
import com.example.demo.user.TypesEnumUser.Types;
import com.example.demo.user.exceptions.EmailAlreadyRegisteredException;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotFoundException;

@Service
public class UserServiceJPA implements UserService {
	private String playgroundName = "playground_lazar";
	
	private UserRepository dataBase;

	private GeneratorService generator;
	
	@Autowired
	public void setDataBase(UserRepository dataBase) {
		this.dataBase = dataBase;
	}
	@Autowired
	public void setGenerator(GeneratorService generator) {
		this.generator = generator;
	}
	

	@Override
	@Transactional
	@MyLog
	public void registerNewUser(UserEntity user) throws EmailAlreadyRegisteredException, InvalidEmailException, InvalidRoleException {
		System.err.println("User Role in Service is: " + user.getRole());
		System.err.println("User Email in Service is: " + user.getEmail());
		if (!isValidEmail(user.getEmail()))
			throw new InvalidEmailException("The email " +user.getEmail()+" is invalid.");
		if (!isValidRole(user.getRole())){
			throw new InvalidRoleException("The entered role: " +user.getRole() +" is not valid!");
		}
		if (!dataBase.existsByEmail(user.getEmail())) {
			user.setCode(generator.generateValidationCode());
			//System.err.println("Code for " + user.getEmail() + ": " + user.getCode()); // Prints the code to the
																						// console
			// generator.stopConsoleForCode(); //Used to halt the system so the code can be
			// copied.

			dataBase.save(user);
		} else
			throw new EmailAlreadyRegisteredException(
					"The email address " + user.getEmail() + " is already registered.");

	}
	
	@Override
	@Transactional
	@MyLog
	public void updateUserInfo(UserEntity user) throws UserNotFoundException, InvalidEmailException {
		if (!isValidEmail(user.getEmail()))
			throw new InvalidEmailException("The email " +user.getEmail()+" is invalid.");
		if (dataBase.existsByEmail(user.getEmail())) {
			dataBase.save(user);
		} else
			throw new UserNotFoundException("The user " + user.getEmail() + " not found.");

	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	public UserEntity getUser(String email, String playground) throws UserNotFoundException {
		if (dataBase.existsByEmail(email)) {
			//UserEntity user = dataBase.findByEmail(email).get();
			if (playground.equals(playgroundName)) {
				
				// TODO: explain to shay
				
				// return dataBase.findByEmail(email).get();
				
				UserEntity ue = dataBase.findByEmail(email).get();
				
				if(ue == null) {
					throw new UserNotFoundException("The email " + email +" was not found.");
				}
				System.err.println("*******" + ue.getEmail());
				return ue;
			}
			else
				throw new UserNotFoundException("The user with id " + email +" and playground " + playgroundName + " not found.");
		} else
			throw new UserNotFoundException("The user with id " + email +" and playground " + playgroundName + " not found.");
	}


	@Override
	@Transactional
	@MyLog
	public void cleanup() {
		dataBase.deleteAll();
	}
	
	private boolean isValidEmail(String email) {
			return Pattern.matches("[_a-zA-Z1-9]+(\\.[A-Za-z0-9]*)*@[A-Za-z0-9]+\\.[A-Za-z0-9]+(\\.[A-Za-z0-9]*)*", email);
	}
	
	public boolean isValidRole(String role) {
		for (Types enumRole : Types.values()) {
			if (role.equals(enumRole.getType()))
					return true;
		}
		return false;
}
	
}
