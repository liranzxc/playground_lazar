package com.example.demo.ElementTests;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;



import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.example.demo.classes.ToClasses.ElementTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementTestShay {

	
	/*
	 * 	====================================== READ ME =====================================================
	 * 	In this test class i do:
	 *  - POST /playground/elements/{userPlayground }/{email}    				I/O:  ElementTO | ElementTO
	 *  - PUT  /playground/elements/{userPlayground}/{email}/{playground}/{id} 	I/O:  ElementTO | ---------
	 *  - GET /playground/elements/{userPlayground}/{email}/{playground}/{id}   I/O:  --------- | ElementTO
	 *  ====================================== READ ME =====================================================
	 */
	
	@LocalServerPort
	private int port;
	private String url;

	RestTemplate rest = new RestTemplate();

	@PostConstruct
	public void init() {

		this.url = "http://localhost:" + port + "/playground/elements";
		System.err.println(this.url);
	}
		
	public void createElement() {
		
	}
	
	public void updateElement() {
		
	}
	
	public void getSpecificElement() {
		
	}
	
	
	@Test
	public void GoToSite()
	{
		
	}
}
