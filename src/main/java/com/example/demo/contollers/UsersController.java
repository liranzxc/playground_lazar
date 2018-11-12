package com.example.demo.contollers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.classes.UserTo;
import com.example.demo.classes.UserEntity;

@RestController
@RequestMapping("playground/users")
public class UsersController {
	//FOR TEST ONLY!
	private String TEST_CODE = "123";

	// user controller
	
	//1. Register a new user. //TODO require testing!!!!
	@RequestMapping(value="/", method=RequestMethod.POST)
	public UserEntity registerFromForm(@RequestBody UserTo userForm) {
		UserEntity user = new UserEntity(userForm);
		return user;
	}
	
	
	//2. Validate code
	@RequestMapping(value="/confirm/{playground}/{email}/{code}", method=RequestMethod.GET
			,produces=MediaType.APPLICATION_JSON_VALUE)
	public UserEntity validateCode(
			@PathVariable("playground") String playground, 
			@PathVariable("email") String email, 
			@PathVariable("code") String code) {
				if (code.equals(TEST_CODE)) {
					return new UserEntity("GOOD CODE", "TEST", "TEST", "TEST", "TEST", new Long(0)); //currently a TEST user.
				}
				else
					return new UserEntity("WRONG CODE", "WRONG CODE", "WRONG CODE", "WRONG CODE", "WRONG CODE", new Long(0)); //Maybe throw an exception, currently returning a "wrong" TEST user
			//TODO in the future we should search for the relevant user in the database and return it.
	}
	
	//3. Log in
	@RequestMapping(value="/login/{playground}/{email}", method=RequestMethod.GET)
	public UserEntity logIn
	(@PathVariable("playground") String playground,
	 @PathVariable("email") String email) {
		return new UserEntity(email, "TEST", "TEST", "TEST", "TEST", new Long(1));
	}
		//the returned user should be searched in the database.
	
	//4. update user by email and playground 
	@RequestMapping(value="/{playground}/{email}" , method=RequestMethod.PUT)
	public void UpdateUser_by_playground_by_email
	(@PathVariable(name="email") String email, 
	@PathVariable(name="playground") String playground)
	{
		//TODO find user by playground and email in DB and update him !
		//throw exception if not found
	}
	
}
