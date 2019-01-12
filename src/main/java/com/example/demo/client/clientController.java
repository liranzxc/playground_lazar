package com.example.demo.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.activity.ActivityService;
import com.example.demo.activity.ActivityTO;
import com.example.demo.activity.ActivityTypes;
import com.example.demo.activity.exceptions.ActivityAlreadyExistException;
import com.example.demo.activity.exceptions.InvalidActivityAtributeException;
import com.example.demo.activity.exceptions.InvalidActivityTypeException;
import com.example.demo.activity.plugins.accessories.Omelette;
import com.example.demo.activity.plugins.accessories.Omelette.EggSize;
import com.example.demo.element.exceptions.ElementAlreadyExistException;
import com.example.demo.element.exceptions.ElementNotFoundException;
import com.example.demo.element.exceptions.InvalidElementForActivityException;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;
import com.example.demo.user.UserTO;
import com.example.demo.user.exceptions.EmailAlreadyRegisteredException;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotFoundException;

@Controller
public class clientController {

	private RestTemplate rest;
	private static int omeletteId = 0;
	private int port = 8080;
	private String url = "http://localhost:" + port + "/playground/users";
	private UserService userService;
	private ActivityService activityService;
	
	@Autowired
	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}

	public clientController() {
		rest = new RestTemplate();
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(path = { "/client", "/" }, method = RequestMethod.GET)
	public ModelAndView Start() {
		ModelAndView model = new ModelAndView();
		model.addObject("userTo", new UserTO());
		model.setViewName("index");
		return model;
	}

	@RequestMapping(path = { "/client", "/" }, method = RequestMethod.POST)
	public ModelAndView LoginPOST(@ModelAttribute UserTO user) {

		ModelAndView model = new ModelAndView();

		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", user.getPlayground());
		map.put("email", user.getEmail());

		try {
			UserTO actual = this.rest.getForObject(this.url + "/login/{playground}/{email}", UserTO.class, map);
			
			//For showing points on log in we need the entity.
			UserEntity userEt = userService.getUser(actual.getEmail(), actual.getPlayground());
			model.addObject("userEt" , userEt);
			
			model.addObject("user", actual);
			model.setViewName("welcomepage");
			return model;
		} catch (Exception e) {
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
			model.setViewName("register2");
			model.addObject("UserTo", new UserTO());
			return model;
		}
	}

	@RequestMapping(path = "/valid", method = RequestMethod.POST)
	public ModelAndView validPOST(@ModelAttribute UserTO user) {

		ModelAndView model = new ModelAndView();
		String code = "";
		System.err.println(user.getEmail());
		System.err.println(user.getPlayground()); // print playground
		try {
			code = userService.getUser(user.getEmail(), user.getPlayground()).getCode();
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("playground", user.getPlayground());
		map.put("email", user.getEmail());
		map.put("code", code);
		
		System.err.println("here test "); // liran test

		this.rest.getForObject(this.url + "/confirm/{playground}/{email}/{code}", UserTO.class, map);

		model.addObject("userTo", new UserTO());
		model.setViewName("index");

		return model;
	}

	@RequestMapping(path = "/createOmlet", method = RequestMethod.GET)
	public ModelAndView createActivity(@RequestParam(name="Email") String Email) throws EmailAlreadyRegisteredException, InvalidEmailException, InvalidRoleException, ElementAlreadyExistException, ActivityAlreadyExistException, InvalidActivityTypeException, InvalidActivityAtributeException, UserNotFoundException, ElementNotFoundException, InvalidElementForActivityException {
		ModelAndView model = new ModelAndView();
		model.setViewName("omelette");
		
		Omelette oml = (Omelette) activityService.addNewActivity(CreateOmlette(Email).ToEntity(), Email);
		model.addObject("ometObject" , oml);
		
		return model;

	}

	private ActivityTO CreateOmlette(String email) throws EmailAlreadyRegisteredException, InvalidEmailException, InvalidRoleException, ElementAlreadyExistException {
		Map <String,Object> map = new HashMap<String,Object>();
		map.put("eggSize", EggSize.Medium);
		
		ActivityTO activityTo = new ActivityTO();
		activityTo.setType(ActivityTypes.CookOmelette.getActivityName());
		activityTo.setPlayerEmail(email);
		activityTo.setId("" +omeletteId++);
		
		
		//TODO give client 2 boxes to fill in order to get element id and playground
		activityTo.setElementId("1");
		activityTo.setElementPlayground("playground_lazar");
		activityTo.setPlayerEmail(email);
		activityTo.setPlayerPlayground("playground_lazar");
		activityTo.setAttributes(map);
		
		return activityTo;


		
	}

}
