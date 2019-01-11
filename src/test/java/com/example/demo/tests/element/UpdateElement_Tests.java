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
import com.example.demo.element.exceptions.ElementNotFoundException;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;
import com.example.demo.user.exceptions.InvalidRoleException;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UpdateElement_Tests {

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
		
		Location demo_entity_location = new Location(0,1);
		this.demo_entity = new ElementEntity(
				"playground_lazar", "1", demo_entity_location.getX(), demo_entity_location.getY()
				,"demo", new Date(), null, "demo type", null, "Aviv", "demo@gmail.com");
	
		this.elementService.cleanup();
		this.userService.cleanup();
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


	// Scenario 1: 
	@Test
	public void updateElementSuccessfullyByManager() throws ElementAlreadyExistException, InvalidRoleException {
		// given
		ElementTO eto = new ElementTO(demo_entity);
		this.elementService.addNewElement(eto.ToEntity(), this.demo_user_manager.getEmail());
		
		String playground = eto.getPlayground();
		String id = eto.getId();

		// when
		this.restTemplate.put(this.url + "/{userPlayground}/{email}/{playground}/{id}", eto,
				demo_user_manager.getPlayground(), demo_user_manager.getEmail(), playground, id);

	}

	// Scenario 2
	@Test
	public void updateElementFailedByPlayer() throws ElementAlreadyExistException, InvalidRoleException {
		// given
		ElementTO eto = new ElementTO(demo_entity);
		this.elementService.addNewElement(eto.ToEntity(), this.demo_user_manager.getEmail());

		String playground = eto.getPlayground();
		String id = eto.getId();

		boolean isSuccess = false;
		// when
		try {
			this.restTemplate.put(this.url + "/{userPlayground}/{email}/{playground}/{id}", eto,
					demo_user_player.getPlayground(), demo_user_player.getEmail(), playground, id);
		} catch (Exception e) {
			isSuccess = true;
		}
		
		assertTrue(isSuccess);
	}

	// Scenario 3:
	@Test(expected = ElementNotFoundException.class)
	public void updateElementThatDoesntExist() throws ElementNotFoundException {
		ElementTO eto = new ElementTO(demo_entity);
		String userPlayground = demo_user_manager.getPlayground();
		String email = demo_user_manager.getEmail();
		String playground = eto.getPlayground();
		String id = eto.getId();

		// when
		try {
			this.restTemplate.put(this.url + "/{userPlayground}/{email}/{playground}/{id}", eto, userPlayground, email,
					playground, id);
		} catch (Exception e) {
			throw new ElementNotFoundException("you cant update an element that doesnt exist");
		}

	}

	
	
}
