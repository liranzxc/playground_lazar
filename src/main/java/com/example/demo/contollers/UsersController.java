package com.example.demo.contollers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.classes.entities.UserEntity;
import com.example.demo.classes.exceptions.EmailAlreadyRegisteredException;
import com.example.demo.classes.exceptions.InvalidConfirmationCodeException;
import com.example.demo.classes.exceptions.UserNotActivatedException;
import com.example.demo.classes.exceptions.UserNotFoundException;

import com.example.demo.classes.to.UserTO;
import com.example.demo.services.userservices.IUserService;
import com.example.demo.services.userservices.UserServiceDummy;

@RestController
@RequestMapping("playground/users")
public class UsersController {
	//FOR TEST ONLY!
	private String TEST_CODE = "123";
	
	@Autowired
	private IUserService userService;
	
	private enum types{
		
		Player("player"),Manager("manger");
		 // declaring private variable for getting values 
	    private String action; 
	  
	    // getter method 
	    public String getAction() 
	    { 
	        return this.action; 
	    } 
	  
	    // enum constructor - cannot be public or protected 
	    private types(String action) 
	    { 
	        this.action = action; 
	    }
	
	};
	

	// user controller

	public String getTEST_CODE() {
		return TEST_CODE;
	}
	

	
	//1. Register a new user.
	@RequestMapping(value="/", method=RequestMethod.POST)
	public UserTO registerFromForm(@RequestBody UserTO userForm) throws EmailAlreadyRegisteredException {
		this.userService.registerNewUser(userForm.ToEntity());
		return userForm;
	}
	
	
	//2. Validate code
	@RequestMapping(value="/confirm/{playground}/{email}/{code}", method=RequestMethod.GET
			,produces=MediaType.APPLICATION_JSON_VALUE)
	public UserTO validateCode(
			@PathVariable("playground") String playground, 
			@PathVariable("email") String email, 
			@PathVariable("code") String code) throws InvalidConfirmationCodeException, UserNotFoundException {
				UserEntity user = userService.getUser(email);
				if (code.equals(user.getCode())) {
					//UserTO validatedUser = new UserTO(userService.getUser(email));
					user.setCode(null);
					userService.updateUserInfo(user);
					return new UserTO(user);
				}
				else
					throw new InvalidConfirmationCodeException();

	}
	
	//3. Log in
	
	@RequestMapping(value="/login/{playground}/{email}", method=RequestMethod.GET)
	public UserEntity logIn
	(@PathVariable("playground") String playground,
	 @PathVariable("email") String email) throws UserNotFoundException {
		return userService.getUser(email);
	}

	
	//4. update user by email and playground 
	@RequestMapping(value="/{playground}/{email}" , method=RequestMethod.PUT,consumes=MediaType.APPLICATION_JSON_VALUE)
	public void UpdateUser_by_playground_by_email
	(@PathVariable(name="email") String email, 
	@PathVariable(name="playground") String playground , @RequestBody UserTO updatedDetails) throws UserNotFoundException, UserNotActivatedException
	{
//		List<UserEntity> mydb = CreateUserDB();
//		
//		UserEntity user =  mydb.stream()
//				.filter(e-> e.getEmail().equals(email) && e.getPlayground().equals(playground))
//				.findFirst().get();

//		if(user!=null && mydb.remove(user))
//			{
//				user.setAvatar(userTo.getAvatar());
//				user.setEmail(userTo.getEmail());
//				user.setRole(userTo.getRole());
//				user.setUsername(userTo.getUsername());
//				
//				mydb.add(user);
//
//				System.out.println("User Update");
		if (userService.getUser(email).getCode()!=null)
			throw new UserNotActivatedException("The user " + email +" is not yet validated!");
		else {
			UserEntity user = userService.getUser(email);
			user.setAvatar(updatedDetails.getAvatar());
			user.setUsername(updatedDetails.getUsername());
			user.setRole(updatedDetails.getRole());
			userService.updateUserInfo(user);
			}
		}

	
	public IUserService getService() {
		return userService;
	}
	
//	public List<UserEntity> CreateUserDB()
//	{
//		return new LinkedList<UserEntity>(Arrays.asList(new UserEntity("lirannh@gmail.com", "play", "liran", "dog",types.Player.getAction(), false),
//				new UserEntity("aviv@gmail.com", "player2", "aviv", "fish",types.Player.getAction(), false)));
//	}
}
