package com.example.demo.client;

import org.hibernate.validator.constraints.SafeHtml.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;
import com.example.demo.user.UserTO;
import com.example.demo.user.exceptions.UserNotFoundException;

@Controller
public class clientController {
	
	private UserService userService;
	
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	@RequestMapping(path="/client",method=RequestMethod.GET)
	public String Start()
	{
		return "index";
	}
	
	@RequestMapping(path="/welcomepage",method=RequestMethod.GET)
	public String welcome()
	{
		return "welcomepage";
	}
	@RequestMapping(path="/register",method=RequestMethod.GET)
	public String register(Model model)
	{
		model.addAttribute("UserTo", new UserTO());
		return "register2";
	}

	@RequestMapping(path="/valid",method=RequestMethod.GET)
	public String valid2(@RequestParam(name="email") String emailUser,  Model model) throws UserNotFoundException
	{
		UserEntity validuser = userService.getUser(emailUser, "playground_lazar");
		model.addAttribute("user", validuser);
		
		return "valid";
	}


}
