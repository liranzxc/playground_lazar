package com.example.demo.tests.element;

import static org.junit.Assert.assertTrue;

import java.util.Date;


import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.element.ElementEntity;
import com.example.demo.element.ElementService;
import com.example.demo.element.ElementServiceJpa;
import com.example.demo.element.ElementTO;
import com.example.demo.element.Location;
import com.example.demo.element.exceptions.ElementAlreadyExistException;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;
import com.example.demo.user.exceptions.InvalidRoleException;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GetAllElements_Tests {

	private int numOfDemoEntities = 5;
	private ElementEntity[] demo_entities;
	private ElementEntity demo_entity;
	
	private UserEntity demo_user_player;
	private UserEntity demo_user_manager;

	/*
	 * ====================================== READ ME
	 * =====================================================
	 * 
	 * In this test class i do: - POST /playground/elements/{userPlayground
	 * }/{email} I/O: ElementTO | ElementTO - PUT
	 * /playground/elements/{userPlayground}/{email}/{playground}/{id} I/O:
	 * ElementTO | --------- - GET
	 * /playground/elements/{userPlayground}/{email}/{playground}/{id} I/O:
	 * --------- | ElementTO
	 * 
	 * 
	 * - GET /{userPlayground}/{email}/all I/O: --------- | ElementTO - GET
	 * /{userPlayground}/{email}/near/{x}/{y}/{distance} I/O: --------- | ElementTO
	 * - GET /{userPlayground}/{email}/search/{attributeName}/{value} I/O: ---------
	 * | ElementTO ====================================== READ ME
	 * =====================================================
	 */

	@LocalServerPort
	private int port;

	private String url;

	private RestTemplate restTemplate;

	@Autowired
	private ElementService elementService;
	
	@Autowired
	private UserService userService;

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port + "/playground/elements";
		
		this.demo_user_manager = new UserEntity("demoManager@gmail.com", "playground_lazar", "mr.manajer", null, "Manager");
		this.demo_user_player = new UserEntity("demoPlayer@gmail.com", "playground_lazar", "mr.palayer", null, "Player");
		
		
		//System.err.println(this.url);
		Location demo_entity_location = new Location(0,1);
		this.demo_entity = new ElementEntity(
				"playground_lazar", "1", demo_entity_location.getX(), demo_entity_location.getY()
				,"demo", new Date(), null, "demo type", null, "Aviv", "demo@gmail.com");
		

		/*
		 * Create numOfDemoEntities element entities more in array for more tests. we used sleep method
		 * for getting different time-stamps.
		 */
		Location demo_entities_locaiton = new Location();
		demo_entities = new ElementEntity[numOfDemoEntities];
		for (int i = 0; i < this.numOfDemoEntities; i++) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.demo_entities[i] = new ElementEntity(
					"playground_lazar", (i+2)+"", demo_entities_locaiton.getX(), demo_entities_locaiton.getY()
					,"demo", new Date(), null, "demo type", null, "Aviv", "demo@gmail.com");
			
			if(i < 2) {
				this.demo_entities[i].setExpireDate(new Date(0));
			}
		}
	}



	@Before
	public void setup() {
		ElementServiceJpa.setIDToZero(); // reset the ID to 0 after each test
	
		try {
			this.userService.registerNewUser(demo_user_manager);
			this.userService.registerNewUser(demo_user_player);
		} catch (Exception e) {
			System.err.println("ElementTest setup exception on registering users, exception is:");
			System.err.println(e.getMessage());
		}
	}
	
	@After
	public void teardown() {
		this.elementService.cleanup();
		this.userService.cleanup();
	}


	///////////////
	// Feature 8 //
	///////////////

	// scenario 1:
	@Test
	public void GetAllElementsSuccessWithOneElement() throws ElementAlreadyExistException, InvalidRoleException {
		// Given:
		this.elementService.addNewElement(this.demo_entity, this.demo_user_manager.getEmail());

		// When:

		ElementTO[] allElements = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/all",
				ElementTO[].class, this.demo_user_manager.getPlayground(), this.demo_user_manager.getEmail());

		// Than:
		boolean success = false;
		if (allElements.length == 1 && allElements[0].equals(new ElementTO(this.demo_entity)))
			success = true;

		assertTrue(success);
	}
	
	// scenario 2:
	@Test
	public void GetNoElementSuccessWithNoElementsInDatabase() {
		
		// Given: nothing
		
		// When:

		ElementTO[] allElements = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/all",
				ElementTO[].class, demo_user_manager.getPlayground(), demo_user_manager.getEmail());

		// Than:
		boolean success = false;
		if (allElements.length == 0)
			success = true;

		assertTrue(success);
	}

	// TODO: change 2 from 5, not 10 from 20
	//scenario 3
	@Test
	public void getAllElementsAsPlayer_GetArrayOfTenElementsWhichIsNotExpired() throws ElementAlreadyExistException, InvalidRoleException {
		for (ElementEntity elementEntity : demo_entities) {
			this.elementService.addNewElement(elementEntity, this.demo_user_manager.getEmail());
		}

		ElementTO[] allElements = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/all",
				ElementTO[].class, demo_user_player.getPlayground(), demo_user_player.getEmail());

		// Than:
		boolean success = false;
		
		if(allElements[allElements.length-1].getId().equals("5")) {
			success = true;
		}

		assertTrue(success);	
	}
	
}
