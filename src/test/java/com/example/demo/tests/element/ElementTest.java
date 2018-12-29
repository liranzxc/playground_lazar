package com.example.demo.tests.element;

import static org.junit.Assert.assertTrue;

import java.util.Date;


import javax.annotation.PostConstruct;

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
public class ElementTest {

	private int numOfDemoEntities = 20;
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
	// private ObjectMapper jsonMapper;

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
			
			if(i == this.numOfDemoEntities/4) {
				this.demo_entities[i].setExpireDate(new Date(1,10,10));
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
	// Feature 5 //
	///////////////

	// Scenario 1
	@Test
	public void createElementSuccsefully() {
//		String usrPlayground = "playground_lazar";
//		String email = "demo@gmail.com";
		ElementTO eto = new ElementTO(this.demo_entity);

		boolean success = false;

		// when
		try {
			this.restTemplate.postForObject(this.url + "/{userPlayground}/{email}", eto, ElementTO.class, demo_user_manager.getPlayground(),
					demo_user_manager.getEmail());
			success = true;
		} catch (Exception e) {
			// do nothing
		}

		// that
		assertTrue(success);
	}

	// Scenario 2
	@Test(expected = InvalidEmailException.class)
	public void createElementWithInvalidEmailAndFail() throws InvalidEmailException {
		String usrPlayground = "playground_lazar";
		String email = "badmail";
		ElementTO eto = new ElementTO(this.demo_entity);

		// when
		try {
			this.restTemplate.postForObject(this.url + "/{userPlayground}/{email}", eto, ElementTO.class, usrPlayground,
					email);
		} catch (Exception e) {
			throw new InvalidEmailException("cant create an element with invalid email: " + email);
		}
	}
	

	
	// Scenario 3
	@Test(expected = ElementAlreadyExistException.class)
	public void createElementWhenElementAlreadyExist() throws ElementAlreadyExistException, InterruptedException {
		// given

		this.elementService.addElementFromOutside(demo_entity);

		// when
		String usrPlayground = "playground_lazar";
		String email = "demo@gmail.com";
		ElementTO eto = new ElementTO(demo_entity);

		try {
			this.restTemplate.postForObject(this.url + "/{userPlayground}/{email}", eto, ElementTO.class, usrPlayground,
					email);
			System.out.println("pass");
		} catch (Exception e) {
			throw new ElementAlreadyExistException();
		}
	}

	

	///////////////
	// Feature 6 //
	///////////////

	// Scenario 1:
	@Test
	public void updateElementSuccessfully() throws ElementAlreadyExistException {
		// given
		ElementTO eto = new ElementTO(demo_entity);
		this.elementService.addNewElement(eto.ToEntity());

		String playground = eto.getPlayground();
		String id = eto.getId();
		
		System.err.println("id = " + id + " playground = " + playground);

		// when
		this.restTemplate.put(this.url + "/{userPlayground}/{email}/{playground}/{id}", eto, demo_user_manager.getPlayground(),demo_user_manager.getEmail(),
				playground, id);

	}

	// Scenario 2:
	@Test(expected = ElementNotFoundException.class)
	public void updateElementThatDoesntExist() throws ElementNotFoundException {
		ElementTO eto = new ElementTO(demo_entity);
		String userPlayground = "lazar_2019";
		String email = "demo@gmail.com";
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

	///////////////
	// Feature 7 //
	///////////////

	// Scenario 1:
	@Test
	public void getSpecificElementSuccess() throws ElementNotFoundException, ElementAlreadyExistException {
		
		// given: an elementEntity with "id":1
		ElementTO originalElementTO = new ElementTO(demo_entity);
		this.elementService.addNewElement(originalElementTO.ToEntity());

		// when
		String userPlayground = "playground_lazar";
		String id = originalElementTO.getId();
		ElementTO elementTOFromDB;
		elementTOFromDB = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/{playground}/{id}",
				ElementTO.class, userPlayground, demo_user_manager.getEmail(), demo_user_manager.getPlayground(), id);

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
	@Test(expected = ElementNotFoundException.class)
	public void getSpecificElementFail() throws ElementNotFoundException {
		// given element not in database (tearDown and setup take care of that)

		String userPlayground = "playground_lazar";
		String email = "demo@gmail.com";
		String playground = "playground_lazar";
		String id = "1";

		try {
			this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/{playground}/{id}", ElementTO.class,
					userPlayground, email, playground, id);
		} catch (Exception e) {
			throw new ElementNotFoundException("element doesnt exist");
		}
	}

	///////////////
	// Feature 8 //
	///////////////

	// scenario 1:
	@Test
	public void GetAllElementsSuccessWithOneElement() throws ElementAlreadyExistException {
		// Given:
		this.elementService.addNewElement(this.demo_entity);

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

	
	//scenario 3
	@Test
	public void getAllElementsAsPlayer() throws ElementAlreadyExistException {
		for (ElementEntity elementEntity : demo_entities) {
			this.elementService.addNewElement(elementEntity);
		}

		ElementTO[] allElements = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/all",
				ElementTO[].class, demo_user_player.getPlayground(), demo_user_player.getEmail());

		// Than:
		boolean success = false;
		
		if(allElements[allElements.length-1].getId().equals("11")) {
			success = true;
		}

		assertTrue(success);	
	}
	

	/////////////////////////////////////////////////
	/////////////////// Feature 9 ///////////////////
	/////////////////////////////////////////////////

	// scenario 1
	@Test
	public void GetOneElementSuccessfulyInDistanceOne() throws ElementAlreadyExistException {

		// Given: an array of one ElementEntity in database with size >0
		this.elementService.addNewElement(this.demo_entity);

		// When:
		double x = this.demo_entity.getX() + 1.0;
		double y = this.demo_entity.getY();
		double distance = 1.0;

		ElementTO[] allElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/near/{x}/{y}/{distance}", ElementTO[].class, this.demo_user_manager.getPlayground(),
				demo_user_manager.getEmail(), x, y, distance);

		// Than:
		boolean success = false;
		if (allElements.length == 1 && allElements[0].equals(new ElementTO(this.demo_entity)))
			success = true;

		assertTrue(success);
	}

	// scenario 2
	@Test
	public void GetElementsFailedWithInvalidDistance()
			throws ElementAlreadyExistException, InvalidDistanceValueException {

		// Given: an array of ElementEntity array is database in size >0
		this.elementService.addNewElement(this.demo_entity);

		// When:
		String userPlayground = "playground_lazar";
		String email = "aviv@gmail.com";
		double x = 1.0, y = 1.0, distance = -1.0;

		// Than:
		boolean success = false;
		try {
			this.restTemplate.getForObject(
					this.url + "/{userPlayground}/{email}/near/{x}/{y}/{distance}", ElementTO[].class, userPlayground,
					email, x, y, distance);
		} catch (Exception e) { // TODO: replace to InvalidDistanceValueException
			success = true;
		}

		assertTrue(success);
	}
	
	// Scenario 3
	@Test
	public void GetNoElementNearLocationZeroZeroAndDistanceOne() throws ElementAlreadyExistException {
		
		// Given: 
		this.elementService.addNewElement(this.demo_entity);
		
		// When:
		double x = 1.0, y = 1.0, distance = 0.0;
		
		// Than:
		boolean success = false;
		
		ElementTO[] allElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/near/{x}/{y}/{distance}", ElementTO[].class, demo_user_manager.getPlayground(),
				demo_user_manager.getEmail(), x, y, distance);
		
		if(allElements.length == 0)
			success = true;
		
		assertTrue(success);
	}
	
	
	// scenario 4 (pagination)
	@Test
	public void GetTheFirstTenResultsFromTwentyElementsInDisanceOneOrLower()
			throws ElementAlreadyExistException, InvalidDistanceValueException {

		for (ElementEntity e : this.demo_entities) {
			if (Integer.parseInt(e.getId()) % 2 == 1) {
				e.setX(Math.random());
				e.setY(0.);
			} else {
				e.setX(0.);
				e.setY(Math.random());
			}
		}

		// Given: 20 elements in distance 1 or lower
		for (ElementEntity e : this.demo_entities) {
			this.elementService.addNewElement(e);
		}

		// When: 
		double x = 0.0, y = 0.0, distance = 1.0;

		// Than:
		ElementTO[] allElements;
		boolean success = false;

		//default size = 10, page = 0;
		allElements = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/near/{x}/{y}/{distance}",
				ElementTO[].class, demo_user_manager.getPlayground(), demo_user_manager.getEmail(), x, y, distance);

		if (allElements.length == 10)
			success = true;

		assertTrue(success);
	}
	
	
	@Test
	public void getAllElementNearByTenAsPlayer() throws ElementAlreadyExistException {

		for (ElementEntity e : this.demo_entities) {
			if (Integer.parseInt(e.getId()) % 2 == 1) {
				e.setX(Math.random());
				e.setY(0.);
			} else {
				e.setX(0.);
				e.setY(Math.random());
			}
		}

		// Given: 20 elements in distance 1 or lower
		for (ElementEntity e : this.demo_entities) {
			this.elementService.addNewElement(e);
		}

		// When: 
		double x = 0.0, y = 0.0, distance = 10.0;

		// Than:
		ElementTO[] allElements;
		boolean success = false;

		//default size = 10, page = 0;
		allElements = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/near/{x}/{y}/{distance}",
				ElementTO[].class, demo_user_player.getPlayground(), demo_user_player.getEmail(), x, y, distance);

		if (allElements[allElements.length-1].getId().equals("11"))
			success = true;

		assertTrue(success);
	}
}
