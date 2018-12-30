package com.example.demo.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.UserService;
import com.example.demo.user.UserTO;
import com.example.demo.user.exceptions.UserNotFoundException;

//@Controller
public class clientController {
	
	private UserService userservice;
	
	@Autowired
	public void setUserservice(UserService userservice) {
		this.userservice = userservice;
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
	public String valid(@RequestParam(name="email") String email ,Model model) throws UserNotFoundException
	{
		model.addAttribute("user", userservice.getUser(email, "playground_lazar"));
		return "valid";
	}


}
