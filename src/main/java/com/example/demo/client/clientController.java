package com.example.demo.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

}
