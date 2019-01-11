package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.exceptions.EmailAlreadyRegisteredException;
import com.example.demo.user.exceptions.InvalidConfirmationCodeException;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotActivatedException;
import com.example.demo.user.exceptions.UserNotFoundException;

@RestController
@RequestMapping("playground/users")
public class UsersController {

	private String TEST_CODE = "1234";

	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getTEST_CODE() {
		return TEST_CODE;
	}


	// 1. Register a new user.
	@RequestMapping(value = "/", method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public UserTO registerFromForm(@RequestBody UserTO userForm, Model model)
			throws EmailAlreadyRegisteredException, InvalidEmailException, InvalidRoleException, UserNotFoundException {

		this.userService.registerNewUser(userForm.ToEntity());
		return userForm;

		
	}


	// 2. Validate code
	@RequestMapping(value = "/confirm/{playground}/{email}/{code}", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public UserTO validateCode(@PathVariable("playground") String playground, @PathVariable("email") String email,
			@PathVariable("code") String code)
			throws InvalidConfirmationCodeException, UserNotFoundException, InvalidEmailException {
		UserEntity user = userService.getUser(email, playground);
		if (code.equals(user.getCode())) {
			// UserTO validatedUser = new UserTO(userService.getUser(email));
			user.setCode(null);
			userService.updateUserInfo(user);

			return new UserTO(user);

		} else {
			throw new UserNotFoundException();
		}

	}

	// 3. Log in

	@RequestMapping(value = "/login/{playground}/{email}", method = RequestMethod.GET)
	public UserTO logIn(@PathVariable("playground") String playground, @PathVariable("email") String email)
			throws UserNotFoundException {

		try {
			UserEntity user = userService.getUser(email, playground);
			return (new UserTO(user));
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new UserNotFoundException();
		}


	}

	// 4. update user by email and playground
	@RequestMapping(value = "/{playground}/{email}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUserByPlaygroundAndEmail(@PathVariable(name = "email") String email,
			@PathVariable(name = "playground") String playground, @RequestBody UserTO updatedDetails)
			throws UserNotFoundException, UserNotActivatedException, InvalidEmailException {

		if (userService.getUser(email, playground).getCode() != null)
			throw new UserNotActivatedException("The user " + email + " is not yet validated!");
		else {
			UserEntity user = userService.getUser(email, playground);
			user.setAvatar(updatedDetails.getAvatar());
			user.setUsername(updatedDetails.getUsername());
			user.setRole(updatedDetails.getRole());
			userService.updateUserInfo(user);
		}
	}

	public UserService getService() {
		return userService;
	}

}
