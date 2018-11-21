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
import com.example.demo.classes.elementAlreadyExistException;
import com.example.demo.classes.EntityClasses.ElementEntity;
import com.example.demo.services.ElementServiceDummy;
import com.example.demo.services.IElementService;

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
	
	@Autowired
	private IElementService elementService;
	

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port + "/playground/elements";
		System.err.println(this.url);
		
		this.demo_entity = new ElementEntity("lazar", "1", new Location(),
											"demo", new Date(), null, "demo type", 
											 null, "Shay", "ggwp@123.com");
	}
		
	@Before
	public void setup() {
		//Do nothing
	}

	@After
	public void teardown() {
		this.elementService.cleanup();
	}

	@Test
	public void createElementSuccsefully() {
		boolean success = false;
		
		//when
		try {
			this.elementService.addNewElement(demo_entity);
			success = true;
		}
		catch(elementAlreadyExistException e) {
			// do nothing
		}
		
		//that
		assertTrue(success);
	}
	
	@Test(expected=elementAlreadyExistException.class)
	public void createElementWhenElementAlreadyExist() throws elementAlreadyExistException {
		//given 
		try {
			this.elementService.addNewElement(demo_entity);
		}
		catch(elementAlreadyExistException e) {
			System.err.println("error occur in test given didnt successed");
		}
		
	
		this.elementService.addNewElement(demo_entity);
	
		
		
	}
	
	@Test
	public void updateElement() {
		
	}
	
	@Test
	public void getSpecificElement() {
		
	}
	
	
}
