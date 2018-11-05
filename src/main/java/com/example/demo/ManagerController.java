package com.example.demo;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.classes.ElementTO;


@RequestMapping(path="/manager")
public class ManagerController {

	// 1. Remove element, path contains the element-id.
	// output: status 200 for success, or 500 for failure.
	@RequestMapping(path = { "/remove/{element-id}" },
			method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Integer>> RegisterPost(@RequestBody ElementTO user)
	{
		if(user != null)
		{
			//TODO add user to DB 
			// AddUser(user);
			return ResponseEntity.ok(Collections.singletonMap("Status", 200));
		}
		else
		{
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Collections.singletonMap("Status", 500));

		}
	}
	
}
