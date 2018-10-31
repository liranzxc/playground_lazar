package com.example.demo;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/general")
public class GeneralController {

	// Example code

//	@RequestMapping(path="/register"
//			,method=RequestMethod.GET
//			,produces=MediaType.TEXT_HTML_VALUE)
//	public String Register()
//	{
//		return "<h3>ERRROR</h3> ";
//	}
//	
//	
//	@RequestMapping(path="/test/{id}"
//			,method=RequestMethod.GET
//			,produces=MediaType.TEXT_HTML_VALUE)
//	public String test(@PathVariable("id") int id)
//	{
//		return id+"";
//	}
//	

	
	// 4. register method post , input from json body ,user data .
	// get status code 
	@RequestMapping(path = { "/register" },
			method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Integer>> RegisterPost(@RequestBody User user)
	{
		if(user != null)
		{
			/// add user to DB 
			// AddUser(user);
			return ResponseEntity.ok(Collections.singletonMap("Status", 200));
		}
		else
		{
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Collections.singletonMap("Status", 500));

		}
	}
	
	
	
	//5. validated method , must input a email and code
	//, the correct code is 123 for all email
	@RequestMapping(path = { "/validate/{email}/{code}","/validate/{email}" },
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	
	public ResponseEntity<Map<String, Integer>> Validated
			(@PathVariable("email") String email,
			@PathVariable(value = "code", required = false) Optional<String> code)
					throws Exception {

		if (code.isPresent()) {
			if (code.get().equals("123")) {
				return ResponseEntity.ok(Collections.singletonMap("Status", 200));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Collections.singletonMap("Status", 500));

			}
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("Code not found", 500));
		}

	}

	
	// 6. login GET method 
	@RequestMapping(path = { "/login"},
			method = RequestMethod.GET, 
			produces = MediaType.TEXT_HTML_VALUE)
	public String Login()
	{
		return "<h1> login page ! </h1>";
	}
	
	//7. Login Post method , require json data with user 
	// for example {"email" : "liran@gmail.com" }
	@RequestMapping(path = { "/login"},
			method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Integer>> LoginPost(@RequestBody User user)
	{
		if(user != null)
		{
			if(user.getEmail().equals("liran@gmail.com"))
			{
				return  ResponseEntity.
						ok(Collections.singletonMap("status", 200));
			}
			else
			{
				return  ResponseEntity.badRequest().
						body(Collections.singletonMap("status", 500));
			}
		}
		else
		{
			// error
			return  ResponseEntity.badRequest().
					body(Collections.singletonMap("status", 501));
		}
	}
	
	
}
