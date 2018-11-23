package com.example.demo.contollers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.classes.EntityClasses.UserEntity;
import com.example.demo.classes.ToClasses.UserTO;
import com.example.demo.classes.exceptions.InvalidCodeException;
import com.example.demo.classes.exceptions.UserNotFoundException;
import com.example.demo.services.IUserService;

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
	
	//1. Register a new user. //TODO require testing!!!!
	@RequestMapping(value="/", method=RequestMethod.POST)
	public UserEntity registerFromForm(@RequestBody UserTO userForm) {
		UserEntity user = new UserEntity(userForm);
		return user;
	}
	
	
	//2. Validate code
	@RequestMapping(value="/confirm/{playground}/{email}/{code}", method=RequestMethod.GET
			,produces=MediaType.APPLICATION_JSON_VALUE)
	public UserEntity validateCode(
			@PathVariable("playground") String playground, 
			@PathVariable("email") String email, 
			@PathVariable("code") String code) throws InvalidCodeException {
				if (code.equals(TEST_CODE)) {
					return new UserEntity("GOOD CODE", "playground_lazar", "TEST", "TEST", "TEST"); //currently a TEST user.
				}
				else
					throw new InvalidCodeException();
			//TODO in the future we should search for the relevant user in the database and return it.
	}
	
	//3. Log in
	@RequestMapping(value="/login/{playground}/{email}", method=RequestMethod.GET)
	public UserEntity logIn
	(@PathVariable("playground") String playground,
	 @PathVariable("email") String email) throws Exception {
		return userService.getUser(email);
	}
		//the returned user should be searched in the database.
	
	//4. update user by email and playground 
	@RequestMapping(value="/{playground}/{email}" , method=RequestMethod.PUT,consumes=MediaType.APPLICATION_JSON_VALUE)
	public void UpdateUser_by_playground_by_email
	(@PathVariable(name="email") String email, 
	@PathVariable(name="playground") String playground , @RequestBody UserTO userTo)
	{
		//TODO find user by playground and email in DB and update him !
		//throw exception if not found
		List<UserEntity> mydb = CreateUserDB();
		
		UserEntity user =  mydb.stream()
				.filter(e-> e.getEmail().equals(email) && e.getPlayground().equals(playground))
				.findFirst().get();
		
		
		if(user!=null && mydb.remove(user))
		{
			user.setAvatar(userTo.getAvatar());
			user.setEmail(userTo.getEmail());
			user.setRole(userTo.getRole());
			user.setUsername(userTo.getUsername());
			
			mydb.add(user);

			System.out.println("User Update");

		}

	}
	
	public IUserService getService() {
		return userService;
	}
	
	public List<UserEntity> CreateUserDB()
	{
		return new LinkedList<UserEntity>(Arrays.asList(new UserEntity("lirannh@gmail.com", "play", "liran", "dog",types.Player.getAction()),
				new UserEntity("aviv@gmail.com", "player2", "aviv", "fish",types.Player.getAction())));
	}
}
