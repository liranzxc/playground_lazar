package com.example.demo.user;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.accessories.GeneratorService;
import com.example.demo.user.exceptions.EmailAlreadyRegisteredException;
import com.example.demo.user.exceptions.InvalidEmailException;
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
	public void registerNewUser(UserEntity user) throws EmailAlreadyRegisteredException, InvalidEmailException {
		if (!isValidEmail(user.getEmail()))
			throw new InvalidEmailException("The email " +user.getEmail()+" is invalid.");
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
	public UserEntity getUser(String email) throws UserNotFoundException {
		if (dataBase.existsByEmail(email)) {
			UserEntity user = dataBase.findByEmail(email).get();
			if (user.getPlayground().equals(playgroundName))
				return dataBase.findByEmail(email).get();
			else
				throw new UserNotFoundException("The user with id " + email +" and playground " + playgroundName + " not found.");
		} else
			throw new UserNotFoundException("The user with id " + email +" and playground " + playgroundName + " not found.");
	}

	// Not needed currently
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

	/*@Override
	@Transactional(readOnly = true)
	public List<UserEntity> getAllUsers(int size, int page)
			throws InvalidPageSizeRequestException, InvalidPageRequestException {
		if (size < 1)
			throw new InvalidPageSizeRequestException();
		if (page < 0)
			throw new InvalidPageRequestException();

		List<UserEntity> list = StreamSupport.stream(dataBase.findAll().spliterator(), false).skip(size * page)
				.limit(size).collect(Collectors.toList());
		return list;
	}
*/
//	@Override
//	@Transactional
//	public List<UserEntity> getAllDeletedUsers() {
//		return null;
//	}

	@Override
	@Transactional
	public void cleanup() {
		dataBase.deleteAll();
	}
	
	public boolean isValidEmail(String email) {
			return Pattern.matches("[_a-zA-Z1-9]+(\\.[A-Za-z0-9]*)*@[A-Za-z0-9]+\\.[A-Za-z0-9]+(\\.[A-Za-z0-9]*)*", email);
	}
	
}
