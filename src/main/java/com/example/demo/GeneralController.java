package com.example.demo;


import java.util.Collections;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/general")
public class GeneralController {
	
	@RequestMapping(path="/register"
			,method=RequestMethod.GET
			,produces=MediaType.TEXT_HTML_VALUE)
	public String Register()
	{
		return "<h3>ERRROR</h3> ";
	}
	
	
	@RequestMapping(path="/test/{id}"
			,method=RequestMethod.GET
			,produces=MediaType.TEXT_HTML_VALUE)
	public String test(@PathVariable("id") int id)
	{
		return id+"";
	}
	
	
	

}
