package com.example.demo.client;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;
import com.example.demo.user.UserTO;
import com.example.demo.user.TypesEnumUser.Types;
import com.example.demo.user.exceptions.UserNotFoundException;

@Controller
public class clientController {

	private RestTemplate rest;
	private int port = 8080;
	private String url = "http://localhost:" + port + "/playground/users";
	private UserService userService;
	public clientController() {
		// TODO Auto-generated constructor stub

		rest = new RestTemplate();
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(path = {"/client","/"}, method = RequestMethod.GET)
	public ModelAndView Start() {
		ModelAndView model = new ModelAndView();
		model.addObject("userTo", new UserTO());
		model.setViewName("index");
		return model;
	}
	
	@RequestMapping(path = {"/client","/"}, method = RequestMethod.POST)
	public ModelAndView LoginPOST(@ModelAttribute UserTO user) {
		
		ModelAndView model = new ModelAndView();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", user.getPlayground());
		map.put("email", user.getEmail());
	
		try
		{
			UserTO actual = this.rest.getForObject(this.url + "/login/{playground}/{email}", UserTO.class, map);
			model.addObject("user", actual);
			model.setViewName("welcomepage");
			return model;
		}
		catch (Exception e) {
			// TODO: handle exception
			model.addObject("userTo", new UserTO());
			model.setViewName("index");

		}

		return model;
		
		
		
	}

	

	@RequestMapping(path = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView model = new ModelAndView();
		model.setViewName("register2");
		model.addObject("UserTo", new UserTO());
		return model;
	}

	@RequestMapping(path = "/register", method = RequestMethod.POST)
	public ModelAndView registerPOST(@ModelAttribute UserTO user) {
		ModelAndView model = new ModelAndView();

		try {
			UserTO userAfterRegister = this.rest.postForObject(this.url + "/", user, UserTO.class);
			
			
			model.setViewName("valid");
			model.addObject("user", userAfterRegister);

			return model;

		} catch (Exception e) {
			// TODO: handle exception
			model.setViewName("register2");
			model.addObject("UserTo", new UserTO());
			return model;
		}
	}
	
	@RequestMapping(path = "/valid", method = RequestMethod.POST)
	public ModelAndView validPOST(@ModelAttribute UserTO user) {
		
		
		System.err.println(user.getEmail());
		System.err.println(user.getPlayground());
		ModelAndView model = new ModelAndView();
		String code = "";
		try {
			code = userService.getUser(user.getEmail(), user.getPlayground()).getCode();
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground",user.getPlayground());
		map.put("email", user.getEmail());
		map.put("code", code);

		
		this.rest.getForObject(this.url + "/confirm/{playground}/{email}/{code}", UserTO.class, map);
		
		model.addObject("userTo", new UserTO());
		model.setViewName("index");
		
		return model;
	}

	
}
