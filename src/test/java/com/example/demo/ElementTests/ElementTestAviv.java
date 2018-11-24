package com.example.demo.ElementTests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.example.demo.classes.Location;
import com.example.demo.classes.EntityClasses.ElementEntity;
import com.example.demo.classes.ToClasses.ElementTO;
import com.example.demo.classes.exceptions.ElementAlreadyExistException;
import com.example.demo.classes.exceptions.InvalidDistanceValueException;
import com.example.demo.services.IElementService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementTestAviv {

	
	@LocalServerPort
	private int port;
	private String url;
	private ElementEntity demo_entity;

	RestTemplate restTemplate;
	
	@Autowired
	private IElementService elementService;

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port + "/playground/elements";
		System.err.println(this.url);
	}
		
	@Before
	public void setup() {
		this.demo_entity = new ElementEntity("playground_lazar", "1", new Location(0, 1), "demo", new Date(), null, "demo type", null,
				"Aviv", "demo@gmail.com");
		
	}
	
	
	@After
	public void teardown() {
		this.elementService.cleanup();
	}
	
	
	// Feature 9 - scenario 1
	@Test
	public void GetAllElementsSuccessWithOneElement() throws ElementAlreadyExistException
	{
		// Given:
		this.elementService.addNewElement(this.demo_entity);
		
		
		// When:
		String userPlayground = "playground_lazar";
		String email = "aviv@gmail.com";
		 
		 ElementTO[] allElements = 
		this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/all" 
				,ElementTO[].class 
				,userPlayground, email);
		
		 // Than:
		boolean success = false;
		if(allElements.length == 1 && allElements[0].equals(new ElementTO(this.demo_entity)))
			success = true;
		
		assertTrue(success);
	}
	
	// TODO: test for pagination - feature 9
	

	// Feature 10 - scenario 1
	@Test
	public void GetAllElementsNearToLocationOneOne() throws ElementAlreadyExistException {
		
		// Given:
		this.elementService.addNewElement(this.demo_entity);
		
		// When:
		String userPlayground = "playground_lazar";
		String email = "aviv@gmail.com";
		double x = 1.0, y = 1.0, distance = 1.0;
		
		 
		 ElementTO[] allElements = 
		this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/near/{x}/{y}/{distance}" 
				,ElementTO[].class 
				,userPlayground, email, x, y, distance);
		
		 // Than:
		boolean success = false;
		if(allElements.length == 1 && allElements[0].equals(new ElementTO(this.demo_entity)))
			success = true;
		
		assertTrue(success);
	}
	
	// Feature 10 - scenario 2
	@Test
	public void GetTheNearElementsWithInvalidDistance() throws ElementAlreadyExistException{
		
		// Given:
		this.elementService.addNewElement(this.demo_entity);
		
		// When:
		String userPlayground = "playground_lazar";
		String email = "aviv@gmail.com";
		double x = 1.0, y = 1.0, distance = -1.0;
		
		// Than:
		ElementTO[] allElements;
		boolean success = false;
		try {
			allElements = 
					this.restTemplate.getForObject(
							this.url + "/{userPlayground}/{email}/near/{x}/{y}/{distance}" 
							,ElementTO[].class 
							,userPlayground, email, x, y, distance);
		}
		catch (Exception e) {  // TODO: replace to InvalidDistanceValueException
			success = true;
		}
		
		assertTrue(success);		
	}
	
	
	// Feature 11 - scenario 1
	@Test
	public void SearchElementByHisID() throws ElementAlreadyExistException {
		// Given:
		this.elementService.addNewElement(this.demo_entity);
		
		// When:
		String userPlayground = "playground_lazar";
		String email = "aviv@gmail.com";
		String attributeName = "id";
		String value = "1";
		
		// Than:
		 ElementTO[] allElements = 
				this.restTemplate.getForObject(
						this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}" 
						,ElementTO[].class 
						,userPlayground, email, attributeName, value);	
		
		boolean success = false;
		
		if(allElements.length == 1 && allElements[0].equals(new ElementTO(this.demo_entity)))
			success = true;
		
		assertTrue(success);				
	}
	
	// Feature 11 - scenario 2
	@Test
	public void SearchElementByInvalidAttributeName() throws ElementAlreadyExistException {
		// Given:
		this.elementService.addNewElement(this.demo_entity);
		
		// When:
		String userPlayground = "playground_lazar";
		String email = "aviv@gmail.com";
		String attributeName = "attack";
		String value = "1";
		
		// Than:
		boolean success = false;
		 ElementTO[] allElements;
		 try {
			 allElements= this.restTemplate.getForObject(
						this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}" 
						,ElementTO[].class 
						,userPlayground, email, attributeName, value);	
		 } catch (Exception e) {  // TODO: replace with InvalidAttributeNameException
			 success = true;
		}
		
		assertTrue(success);			
	}
	
	
}
	
	
	
	
