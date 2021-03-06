package com.example.demo.tests.element;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

import com.example.demo.aop.EmailValue;
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
public class SearchElementByAttributes_Tests {

	private int numOfEntities = 5;
	private ElementEntity[] demo_entities;
	private ElementEntity demo_entity;

	private UserEntity demo_user_player;
	private UserEntity demo_user_manager;

	/*
	 * ====================================== READ ME
	 * =====================================================
	 * 
	 * In this test class i do: - GET
	 * /{userPlayground}/{email}/search/{attributeName}/{value} I/O: --------- |
	 * ElementTO ====================================== READ ME
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
		// this.jsonMapper = new ObjectMapper();
		this.url = "http://localhost:" + port + "/playground/elements";

		
		this.demo_user_manager = new UserEntity("demoManager@gmail.com", "playground_lazar", "mr.manajer", null, "Manager");
		this.demo_user_player = new UserEntity("demoPlayer@gmail.com", "playground_lazar", "mr.palayer", null, "Player");
		
		Location demo_entity_location = new Location(0, 1);
		this.demo_entity = new ElementEntity("playground_lazar", "0", demo_entity_location.getX(),
				demo_entity_location.getY(), "demo", new Date(), null, "demo type", null, "Aviv", "demo@gmail.com");
		/*
		 * Create numOfDemoEntities element entities more in array for more tests. we
		 * used sleep method for getting different time-stamps.
		 */
		Location demo_entities_locaiton = new Location();
		demo_entities = new ElementEntity[this.numOfEntities];
		for (int i = 0; i < this.numOfEntities; i++) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.demo_entities[i] = new ElementEntity("playground_lazar", (i + 1) + "", demo_entities_locaiton.getX(),
					demo_entities_locaiton.getY(), "demo", new Date(), null, "demo type", null, "Aviv",
					"demo@gmail.com");
		}
		
		this.elementService.cleanup();
		this.userService.cleanup();
	}

	@Before
	public void setup() throws InterruptedException {
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



	// scenario 1 - NAME\Manager
	@Test
	public void searchOneElementWithHisNameByManagerSuccessfulyInDatabaseWithOneElement() throws ElementAlreadyExistException, InvalidRoleException {
		assertTrue(scenario1("name", this.demo_entity.getName()));
	}

	// scenario 2 - TYPE\Manager
	@Test
	public void searchOneElementWithHisTypeByManagerSuccessfullyInDatabaseWithOneElement() throws ElementAlreadyExistException, InvalidRoleException {
		assertTrue(scenario1("type", this.demo_entity.getType()));
	}
	
	// scenario 3 - TYPE\player 
	@Test
	public void searchOneExpiredElementWithHisTypeByPlayer_Failed()
			throws ElementAlreadyExistException, InterruptedException, InvalidRoleException {
		// Given:
		demo_entity.setExpireDate(new Date(0));
		this.elementService.addNewElement(this.demo_entity, this.demo_user_manager.getEmail());
		

			ElementTO[] etos = this.restTemplate.getForObject(
					this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
					demo_user_player.getPlayground(), demo_user_player.getEmail(), "type", this.demo_entity.getType());
		
		// Than:
		assertTrue(etos.length == 0);
	}

	private boolean scenario1(String attributeName, String value) throws ElementAlreadyExistException, InvalidRoleException {
		// Given:
		this.elementService.addNewElement(this.demo_entity, this.demo_user_manager.getEmail());

		// When:

		// Than:
		ElementTO[] allElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
				demo_user_manager.getPlayground(), demo_user_manager.getEmail(), attributeName, value);

		boolean isSuccess = false;

		if (allElements.length == 1 && allElements[0].equals(new ElementTO(this.demo_entity)))
			isSuccess = true;

		return isSuccess;
	}

	// scenario 4 - NAME
	@Test
	public void searchElementByNameSuccessfulyInDatabaseWithTwentyElement() throws ElementAlreadyExistException, InvalidRoleException {
		// Given: 5 element entities in database (and one is the target)
		for (ElementEntity e : this.demo_entities) {
			if (Integer.parseInt(e.getId()) == 2) {
				e.setName("demo_target"); // the test
			}
			this.elementService.addNewElement(e, this.demo_user_manager.getEmail());
		}

		// When:
		String attributeName = "name";
		String value = "demo_target";

		// Than:
		ElementTO[] allElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
				demo_user_manager.getPlayground(), demo_user_manager.getEmail() , attributeName, value);

		boolean success = false;
		
		if (allElements.length == 1 && allElements[0].getName().equals(value))
			success = true;

		assertTrue(success);
	}

	// scenario 5 - TYPE
	@Test
	public void searchElementByTypeSuccessfulyInDatabaseWithTwentyElements() throws ElementAlreadyExistException, InvalidRoleException {
		// Given: 5 element entities in database (and one is the target)
		for (ElementEntity e : this.demo_entities) {
			if (Integer.parseInt(e.getId()) == 2) {
				e.setType("demo_target"); // the test
			}
			this.elementService.addNewElement(e, this.demo_user_manager.getEmail());
		}

		// When:
		String attributeName = "type";
		String value = "demo_target";

		// Than:
		ElementTO[] allElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
				demo_user_manager.getPlayground(), demo_user_manager.getEmail(), attributeName, value);

		boolean success = false;

		if (allElements.length == 1 && allElements[0].getType().equals(value))
			success = true;

		assertTrue(success);
	}

	
	// scenario 6 - NAME
	@Test
	public void searchTwoElementsByNameSuccessfulyInDatabaseWithFiveElements() throws ElementAlreadyExistException, InvalidRoleException {
		// Given:

		for (int i = 0; i < this.demo_entities.length; i++) {
			if (i < 2) {
				demo_entities[i].setName("demo_target");
			}
			this.elementService.addNewElement(demo_entities[i], this.demo_user_manager.getEmail());
		}

		// When:
		String attributeName = "name";
		String value = "demo_target";

		// Than:
		ElementTO[] allElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
				demo_user_manager.getPlayground(), demo_user_manager.getEmail(), attributeName, value);

		boolean success = true;

		if (allElements.length != 2) {
			success = false;
		}

		for (int i = 0; i < allElements.length; i++) {
			if (!value.equals(allElements[i].getName())) {
				success = false;
			}
		}
		assertTrue(success);
	}


	// scenario 7 - TYPE
	@Test
	public void searchTwoElementsByTypeSuccessfulyInDatabaseWithFiveElements() throws ElementAlreadyExistException, InvalidRoleException {
		// Given: 

		for (int i = 0; i < this.demo_entities.length; i++) {
			if (i < 2) {
				demo_entities[i].setType("demo_target");
			}
			this.elementService.addNewElement(demo_entities[i], this.demo_user_manager.getEmail());
		}

		// When:
		String attributeName = "type";
		String value = "demo_target";

		// Than:
		ElementTO[] allElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
				demo_user_manager.getPlayground(), demo_user_manager.getEmail(), attributeName, value);

		boolean success = true;

		if (allElements.length != 2) {
			success = false;
		}

		for (int i = 0; i < allElements.length; i++) {
			if (!value.equals(allElements[i].getType())) {
				success = false;
			}
		}

		assertTrue(success);
	}

	// scenario 8
	@Test
	public void searchElementFailedByInvalidAttributeNameInDatabaseWithOneElement() throws ElementAlreadyExistException, InvalidRoleException {
		// Given:
		this.elementService.addNewElement(this.demo_entity, this.demo_user_manager.getEmail());

		// When:
		String attributeName = "attack";
		String value = "1";

		// Than:
		boolean success = false;

		try {
			this.restTemplate.getForObject(
					this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
					demo_user_manager.getPlayground(), demo_user_manager.getEmail(), attributeName, value);
		} catch (Exception e) {
			success = true;
		}

		assertTrue(success);
	}

	
	// scenario 9 - Name
	@Test
	public void searchNoElementByNameInDatabaseWithFiveElements() throws ElementAlreadyExistException, InvalidRoleException {
		assertTrue(scenario5("name", "no demo name"));
	}

	// scenario 10 - Type
	@Test
	public void searchNoElementByTypeInDatabaseWithFiveElements() throws ElementAlreadyExistException, InvalidRoleException {
		assertTrue(scenario5("type", "no demo type"));
	}

	private boolean scenario5(String attributeName, String value) throws ElementAlreadyExistException, InvalidRoleException {
		// Given:
		ArrayList<ElementEntity> demo_targets = new ArrayList<>();

		for (ElementEntity e : this.demo_entities) {
			this.elementService.addNewElement(e, this.demo_user_manager.getEmail());
		}

		demo_targets.trimToSize();

		// When:


		// Than:
		boolean success = false;

		ElementTO[] allElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
				demo_user_manager.getPlayground(), demo_user_manager.getEmail(), attributeName, value);

		if (allElements.length == 0)
			success = true;

		return success;
	}

	// scenario 11 - Name
	@Test
	public void CheckDefaultPagination_searchTenElementsByNameSuccessfulyInDatabaseWithTwentyElements()
			throws ElementAlreadyExistException, InvalidRoleException {
		assertTrue(scenario6("name", "demo"));
	}

	// scenario 12 - Type
	@Test
	public void CheckDefaultPagination_searchTenElementsByTypeSuccessfulyInDatabaseWithTwentyElements()
			throws ElementAlreadyExistException, InvalidRoleException {
		assertTrue(scenario6("type", "demo type"));
	}

	private boolean scenario6(String attributeName, String value) throws ElementAlreadyExistException, InvalidRoleException {

		ElementEntity[] test_entities = new ElementEntity[11];
		
		for (int i = 0; i < test_entities.length; i++) {
			test_entities[i] = new ElementEntity("playground_lazar", (i + 1) + "", 0,
					0, "demo", new Date(), null, "demo type", null, "Aviv",
					"demo@gmail.com");
			
		}
		
		for (ElementEntity e : test_entities) {
			this.elementService.addNewElement(e, this.demo_user_manager.getEmail());
		}

		// When:

		// Than:
		ElementTO[] allElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
				demo_user_manager.getPlayground(), demo_user_manager.getEmail(), attributeName, value);

		boolean success = true;

		if (allElements.length != 10) {
			success = false;
		}

		return success;
	}

	// scenario 13 - Name
	@Test
	public void CheckPagination_searchSevenElementsInPageOneByNameSuccessfulyInDatabase()
			throws ElementAlreadyExistException, InvalidRoleException {
		assertTrue(scenario7("name", "demo"));
	}
	
	// scenario 14 - Type
		@Test
		public void CheckPagination_searchSevenElementsInPageOneByTypeSuccessfulyInDatabase()
				throws ElementAlreadyExistException, InvalidRoleException {
			assertTrue(scenario7("type", "demo type"));
		}

	private boolean scenario7(String attributeName, String value) throws ElementAlreadyExistException, InvalidRoleException {
		// Given: 20 elements entities in database (which 5 of them are the targets)
		for (ElementEntity e : this.demo_entities) {
			this.elementService.addNewElement(e, this.demo_user_manager.getEmail());
		}

		// When:
		int size = 2;
		int page = 1;

		Map<String, Object> map = new HashMap<>();
		map.put("userPlayground", demo_user_manager.getPlayground());
		map.put("email", demo_user_manager.getEmail());
		map.put("attributeName", attributeName);
		map.put("value", value);
		map.put("size", size);
		map.put("page", page);

		// Than:
		ElementTO[] allElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}?size={size}&page={page}",
				ElementTO[].class, map);

		boolean success = false;

		for (ElementTO elementTO : allElements) {
			System.err.println(elementTO);
		}

		if (allElements.length == size && allElements[0].getId().equals((size + 1) + "")) {
			success = true;
		}

		return success;
	}
	
}
