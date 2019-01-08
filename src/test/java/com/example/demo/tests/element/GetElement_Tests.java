package com.example.demo.tests.element;

import static org.junit.Assert.assertTrue;

import java.util.Date;


import javax.annotation.PostConstruct;
import javax.validation.constraints.AssertTrue;

import org.hibernate.service.spi.Manageable;
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
import com.example.demo.element.exceptions.InvalidDistanceValueException;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;
import com.example.demo.user.exceptions.EmailAlreadyRegisteredException;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.InvalidRoleException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GetElement_Tests {

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
	public void getSpecificElementSuccessByPlayer() throws ElementNotFoundException, ElementAlreadyExistException, InvalidRoleException {
		
		// given: an elementEntity with "id":1
		ElementTO originalElementTO = new ElementTO(demo_entity);
		this.elementService.addNewElement(originalElementTO.ToEntity(), this.demo_user_manager.getEmail());

		// when
		String userPlayground = "playground_lazar";
		String id = originalElementTO.getId();
		ElementTO elementTOFromDB;
		elementTOFromDB = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/{playground}/{id}",
				ElementTO.class, userPlayground, demo_user_player.getEmail(), demo_user_player.getPlayground(), id);

		// Than
		boolean success = true;
		if (!elementTOFromDB.getId().equals(originalElementTO.getId())) {
			success = false;
		} else if (!elementTOFromDB.getPlayground().equals(originalElementTO.getPlayground())) {
			success = false;
		}

		assertTrue(success);
	}

	// Scenario 2:
	@Test
	public void getSpecificElementWithExpiredDate_FailedByPlayer() throws ElementNotFoundException, ElementAlreadyExistException, InterruptedException, InvalidRoleException {

		demo_entity.setExpireDate(new Date());
		Thread.sleep(50);
		
		ElementTO originalElementTO = new ElementTO(demo_entity);
		this.elementService.addNewElement(originalElementTO.ToEntity(), this.demo_user_manager.getEmail());

		// when
		String userPlayground = "playground_lazar";
		String id = originalElementTO.getId();
		
		boolean success = false;
		try {
			this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/{playground}/{id}",
					ElementTO.class, userPlayground, demo_user_player.getEmail(), demo_user_player.getPlayground(), id);
		}catch (Exception e) {
			success = true;
		}
		
		// Than
		assertTrue(success);
	}

	// Scenario 3
	@Test
	public void getSpecificElementWithExpiredDate_SuccessByManager()
			throws ElementNotFoundException, ElementAlreadyExistException, InterruptedException, InvalidRoleException {

		demo_entity.setExpireDate(new Date());
		Thread.sleep(50);

		ElementTO originalElementTO = new ElementTO(demo_entity);
		this.elementService.addNewElement(originalElementTO.ToEntity(), this.demo_user_manager.getEmail());

		// when
		String elementPlayground = originalElementTO.getPlayground();
		String id = originalElementTO.getId();

		boolean success = false;
		try {
			this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/{playground}/{id}", ElementTO.class,
					demo_user_manager.getPlayground(), demo_user_manager.getEmail(), elementPlayground, id);
			success = true;
		} catch (Exception e) {
			// do nothing
		}

		// Than
		assertTrue(success);
	}

	// Scenario 4:
	@Test(expected = ElementNotFoundException.class)
	public void getSpecificElementFailWhenDataBaseIsEmpty() throws ElementNotFoundException {
		// given element not in database (tearDown and setup take care of that)

		String elementPlayground = "playground_lazar";
		String id = "1";

		try {
			this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/{playground}/{id}", ElementTO.class,
					demo_user_manager.getPlayground(), demo_user_manager.getEmail(), elementPlayground, id);
		} catch (Exception e) {
			throw new ElementNotFoundException("element doesnt exist");
		}
	}
	
}
