package com.example.demo.tests.element;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.client.RestTemplate;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.classes.Location;
import com.example.demo.classes.entities.ElementEntity;
import com.example.demo.classes.exceptions.ElementAlreadyExistException;
import com.example.demo.classes.exceptions.ElementNotFoundException;
import com.example.demo.classes.exceptions.InvalidDistanceValueException;
import com.example.demo.classes.exceptions.InvalidEmailException;
import com.example.demo.classes.exceptions.InvalidPageRequestException;
import com.example.demo.classes.exceptions.InvalidPageSizeRequestException;
import com.example.demo.classes.to.ElementTO;
import com.example.demo.services.elementservices.IElementService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementTest {

	private int numOfDemoEntities = 20;
	private ElementEntity[] demo_entities;
	private ElementEntity demo_entity;

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
	private IElementService elementService;

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port + "/playground/elements";

		//System.err.println(this.url);
		ElementEntity.zeroID();
		this.demo_entity = new ElementEntity("playground_lazar", new Location(0, 1), "demo", new Date(), null,
				"demo type", null, "Aviv", "demo@gmail.com");

		/*
		 * Create numOfDemoEntities element entities more in array for more tests. we used sleep method
		 * for getting different time-stamps.
		 */
		demo_entities = new ElementEntity[numOfDemoEntities];
		for (int i = 0; i < this.numOfDemoEntities; i++) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.demo_entities[i] = new ElementEntity("playground_lazar"+i, new Location(), "demo",
					new Date(), null, "demo type", null, "Aviv", "demo@gmail.com");
		}
	}



	@Before
	public void setup() throws InterruptedException {
	}
	
	@After
	public void teardown() {
		this.elementService.cleanup();
	}

	///////////////
	// Feature 5 //
	///////////////

	// Scenario 1
	@Test
	public void createElementSuccsefully() {
		String usrPlayground = "playground_lazar";
		String email = "demo@gmail.com";
		ElementTO eto = new ElementTO(this.demo_entity);

		boolean success = false;

		// when
		try {
			this.restTemplate.postForObject(this.url + "/{userPlayground}/{email}", eto, ElementTO.class, usrPlayground,
					email);
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
	public void createElementWhenElementAlreadyExist() throws ElementAlreadyExistException {
		// given
		this.elementService.addNewElement(demo_entity);

		// when
		String usrPlayground = "playground_lazar";
		String email = "demo@gmail.com";
		ElementTO eto = new ElementTO(demo_entity);

		try {
			this.restTemplate.postForObject(this.url + "/{userPlayground}/{email}", eto, ElementTO.class, usrPlayground,
					email);

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

		String userPlayground = "lazar_2019";
		String email = "demo@gmail.com";
		String playground = eto.getPlayground();
		String id = eto.getId();

		// when
		this.restTemplate.put(this.url + "/{userPlayground}/{email}/{playground}/{id}", eto, userPlayground, email,
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
		String email = "demo@gmail.com";
		String playground = originalElementTO.getPlayground();
		String id = originalElementTO.getId();
		ElementTO elementTOFromDB;
		elementTOFromDB = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/{playground}/{id}",
				ElementTO.class, userPlayground, email, playground, id);

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
		String userPlayground = "playground_lazar";
		String email = "demo@gmail.com";

		ElementTO[] allElements = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/all",
				ElementTO[].class, userPlayground, email);

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
		String userPlayground = "playground_lazar";
		String email = "demo@gmail.com";

		ElementTO[] allElements = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/all",
				ElementTO[].class, userPlayground, email);

		// Than:
		boolean success = false;
		if (allElements.length == 0)
			success = true;

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
		String userPlayground = "playground_lazar";
		String email = "aviv@gmail.com";
		double x = this.demo_entity.getX() + 1.0;
		double y = this.demo_entity.getY();
		double distance = 1.0;

		ElementTO[] allElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/near/{x}/{y}/{distance}", ElementTO[].class, userPlayground,
				email, x, y, distance);

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
			ElementTO[] allElements = this.restTemplate.getForObject(
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
		String userPlayground = "playground_lazar";
		String email = "aviv@gmail.com";
		double x = 1.0, y = 1.0, distance = 0.0;
		
		// Than:
		boolean success = false;
		
		ElementTO[] allElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/near/{x}/{y}/{distance}", ElementTO[].class, userPlayground,
				email, x, y, distance);
		
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
		String userPlayground = "playground_lazar";
		String email = "aviv@gmail.com";
		double x = 0.0, y = 0.0, distance = 1.0;

		// Than:
		ElementTO[] allElements;
		boolean success = false;

		//default size = 10, page = 0;
		allElements = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/near/{x}/{y}/{distance}",
				ElementTO[].class, userPlayground, email, x, y, distance);

		if (allElements.length == 10)
			success = true;

		assertTrue(success);
	}
}
