package com.example.demo.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("playground/activites")
public class ActivitesController {

	
	@Autowired
	ActivityRepository activityRepository;
	
	// activites controller

	// 11. go to some activity and do something and return object

	@RequestMapping(path = "/{userPlayground}/{email}", 
			method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object GoToActivity(@RequestBody ActivityTO activity,
			@PathVariable(name = "userPlayground") String userPlayground, 
			@PathVariable(name = "email") String email) {
		
			activityRepository.save(activity.ToEntity());
		
		// just return activity for testing - Checked !
		return activity;

	}

}
