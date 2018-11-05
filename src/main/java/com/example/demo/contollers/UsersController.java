package com.example.demo.contollers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.classes.NewUserForm;
import com.example.demo.classes.UserTO;

@RestController
@RequestMapping("playground/users")
public class UsersController {
	//FOR TEST ONLY!
	private String TEST_CODE = "123";

	// user controller
	
	//1. Register a new user. //TODO require testing!!!!
	@RequestMapping(value="", method=RequestMethod.POST)
	public UserTO registerFromForm(@RequestBody NewUserForm userForm) {
		UserTO user = new UserTO(userForm);
		return user;
	}
	
	//2. Validate code
	@RequestMapping(value="/confirm/{playground}/{email}/{code}", method=RequestMethod.GET)
	public UserTO validateCode(
			@PathVariable("playground") String playground, 
			@PathVariable("email") String email, 
			@PathVariable("code") String code) {
				if (code.equals(TEST_CODE)) {
					return new UserTO("GOOD CODE", "TEST", "TEST", "TEST", "TEST", null); //currently a TEST user.
				}
				else
					return new UserTO("WRONG CODE", "WRONG CODE", "WRONG CODE", "WRONG CODE", "WRONG CODE", null); //Maybe throw an exception, currently returning a "wrong" TEST user
			//TODO in the future we should search for the relevant user in the database and return it.
	}
	
	//3. Log in
	@RequestMapping(value="/login/{playground}/{email}", method=RequestMethod.GET)
	public UserTO logIn
	(@PathVariable("playground") String playground,
	 @PathVariable("email") String email) {
		return new UserTO("TEST", "TEST", "TEST", "TEST", "TEST", null);
	}
		//the returned user should be searched in the database.
	
	//4. update user by email and playground 
	@RequestMapping(value="/{playground}/{email}" , method=RequestMethod.PUT)
	public void UpdateUser_by_playground_by_email
	(@PathVariable(name="email") String email, 
	@PathVariable(name="playground") String playground)
	{
		// find user by playground and email in DB and update him !
	}
	
}
