package com.example.demo.ElementTests;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.hibernate.validator.internal.constraintvalidators.bv.AssertTrueValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;



import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.classes.Location;
import com.example.demo.classes.EntityClasses.ElementEntity;
import com.example.demo.classes.ToClasses.ElementTO;
import com.example.demo.classes.exceptions.elementAlreadyExistException;
import com.example.demo.services.ElementServiceDummy;
import com.example.demo.services.IElementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementTestShay {

	private ElementEntity demo_entity;
	
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
	
	private RestTemplate restTemplate;
	private ObjectMapper jsonMapper;
	
	@Autowired
	private IElementService elementService;
	

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.jsonMapper = new ObjectMapper();
		this.url = "http://localhost:" + port + "/playground/elements";
		
		System.err.println(this.url);
		
	}
		
	@Before
	public void setup() {

		this.demo_entity = new ElementEntity("lazar", "1", new Location(),
											"demo", new Date(), null, "demo type", 
											 null, "Shay", "ggwp@123.com");
	}

	@After
	public void teardown() {
		this.elementService.cleanup();
	}

	@Test
	public void createElementSuccsefully() {
		String usrPlayground = "lazar_playground";
		String email = "demo@gmail.com";
		ElementTO eto = new ElementTO(this.demo_entity);
		
		boolean success = false;
		
		//when
		try {
			this.restTemplate.postForObject(this.url +  "/{userPlayground}/{email}", eto, ElementTO.class, usrPlayground, email);
			success = true;
		}
		catch(Exception e) {
			// do nothing
		}
		
		//that
		assertTrue(success);
	}
	
	@Test(expected=elementAlreadyExistException.class)
	public void createElementWhenElementAlreadyExist() throws elementAlreadyExistException {
		//given 
		String userPlayground = "lazar_playground";
		String email = "demo@gmail.com";
		ElementTO eto = new ElementTO(demo_entity);
		
//		boolean dataEnterd = false;
//		
//		try {
//			this.elementService.addNewElement(eto.ToEntity());
//			dataEnterd = true;
//		}
//		catch(elementAlreadyExistException e) {
//			//do nothing
//		}
//		
//		if(dataEnterd) {
//			this.restTemplate.postForObject(this.url +  "/{userPlayground}/{email}", eto, ElementTO.class, userPlayground, email);
//		}
//		
		boolean dataEnterd = false;
		
		try {
			this.elementService.addNewElement(eto.ToEntity());
			dataEnterd = true;
		}
		catch(elementAlreadyExistException e) {
			//do nothing
		}
		
		if(dataEnterd) {
			this.elementService.addNewElement(eto.ToEntity());
		}
	}
	
	@Test
	public void updateElement() {
		
	}
	
	@Test
	public void getSpecificElement() {
		
	}
	
	
}
