package com.example.demo.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.user.exceptions.EmailAlreadyRegisteredException;
import com.example.demo.user.exceptions.InvalidConfirmationCodeException;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotActivatedException;
import com.example.demo.user.exceptions.UserNotFoundException;

@Controller
@RequestMapping("playground/users")
public class UsersController {
	//FOR TEST ONLY!
	private String TEST_CODE = "1234";
	
	private UserService userService;
	
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	public String getTEST_CODE() {
		return TEST_CODE;
	}
	
	

	
	//1. Register a new user.
	@RequestMapping(value="/", method=RequestMethod.POST)
	public String registerFromForm(@ModelAttribute UserTO userForm,Model model) throws EmailAlreadyRegisteredException, InvalidEmailException, InvalidRoleException, UserNotFoundException {
				
		try {
			this.userService.registerNewUser(userForm.ToEntity());
			return "redirect:/valid?email="+userForm.getEmail();
		
		}
		catch (Exception e) {
			// TODO: handle exception
			
			
			return "redirect:/register";

		}
		
		
	}
	
	
	//2. Validate code
	@RequestMapping(value="/confirm/{playground}/{email}/{code}", method=RequestMethod.GET
			,produces=MediaType.APPLICATION_JSON_VALUE)
	public String validateCode(
			@PathVariable("playground") String playground, 
			@PathVariable("email") String email, 
			@PathVariable("code") String code) throws InvalidConfirmationCodeException, UserNotFoundException {
				UserEntity user = userService.getUser(email, playground);
				if (code.equals(user.getCode())) {
					//UserTO validatedUser = new UserTO(userService.getUser(email));
					user.setCode(null);
					try {
						userService.updateUserInfo(user);
					} catch (InvalidEmailException e) { //This case should not happen, because we only update the user's code.
						e.printStackTrace();
					}
				//	return new UserTO(user);
					
					return "redirect:/client";
					
				}
				else
					throw new InvalidConfirmationCodeException();

	}
	
	//3. Log in
	
	@RequestMapping(value="/login/{playground}/{email}", method=RequestMethod.GET)
	public String logIn
	(@PathVariable("playground") String playground,
	 @PathVariable("email") String email) throws UserNotFoundException {
		
		
		try {
			UserEntity user = userService.getUser(email, playground);
		
			return "redirect:/welcomepage";
					}
		catch (Exception e) {
			// TODO: handle exception
		
			return "redirect:/client";
		}
		

		
		
	}

	
	//4. update user by email and playground 
	@RequestMapping(value="/{playground}/{email}" , method=RequestMethod.PUT,consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateUserByPlaygroundAndEmail
	(@PathVariable(name="email") String email, 
	@PathVariable(name="playground") String playground , @RequestBody UserTO updatedDetails) throws UserNotFoundException, UserNotActivatedException, InvalidEmailException
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
		if (userService.getUser(email, playground).getCode()!=null)
			throw new UserNotActivatedException("The user " + email +" is not yet validated!");
		else {
			UserEntity user = userService.getUser(email,playground);
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
