package com.example.demo.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.UserTO;

@Controller
public class clientController {
	
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
	public String valid(Model model)
	{
		return "valid";
	}


}
