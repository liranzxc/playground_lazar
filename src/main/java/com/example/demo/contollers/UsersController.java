package com.example.demo.contollers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.classes.UserTo;

@RestController
@RequestMapping("playground/users")
public class UsersController {

	// user controller
	
	//4. update user by email and playground 
	@RequestMapping(value="/{playground}/{email}" , method=RequestMethod.PUT)
	public void UpdateUser_by_playground_by_email
	(@PathVariable(name="email") String email, 
	@PathVariable(name="playground") String playground)
	{
		// find user by playground and email in DB and update him !
	}
	
}
