package com.example.demo.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.activity.exceptions.ActivityAlreadyExistException;
import com.example.demo.activity.exceptions.InvalidActivityAtributeException;
import com.example.demo.activity.exceptions.InvalidActivityTypeException;
import com.example.demo.element.exceptions.ElementNotFoundException;
import com.example.demo.element.exceptions.InvalidElementForActivityException;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotFoundException;

@RestController
@RequestMapping("playground/activites")
public class ActivitesController {
	
	private ActivityService service;
	
	
	@Autowired
	public void setActivityRepository(ActivityService activityService) {
		this.service = activityService;
	}
	
	
	// activites controller

	// 11. go to some activity and do something and return object

	@RequestMapping(path = "/{userPlayground}/{email}", 
			method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object CreateActivity(@RequestBody ActivityTO activity,
			@PathVariable(name = "userPlayground") String userPlayground, 
			@PathVariable(name = "email") String email) 
					throws ActivityAlreadyExistException, InvalidRoleException, 
					InvalidActivityTypeException, InvalidActivityAtributeException, UserNotFoundException
					, InvalidEmailException, ElementNotFoundException, InvalidElementForActivityException {
		
		//this.elementVerifyier.verifyElement(activity.getElementPlayground(), activity.getElementId());
		//System.err.println("In activity controller - the email is: " + email);
		service.addNewActivity(activity.ToEntity(), email);
		// just return activity for testing - Checked !
		return activity;

	}

}
