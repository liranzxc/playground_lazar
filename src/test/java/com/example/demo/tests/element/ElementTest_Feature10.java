package com.example.demo.tests.element;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

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
import com.example.demo.element.ElementTO;
import com.example.demo.element.ElementService;
import com.example.demo.element.Location;
import com.example.demo.element.exceptions.ElementAlreadyExistException;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementTest_Feature10 {

	private int numOfEntities = 10;
	private ElementEntity[] demo_entities;
	private ElementEntity demo_entity;

	/*
	 * ====================================== READ ME ===================================================== 
	 * 
	 * In this test class i do: 
	 * - GET /{userPlayground}/{email}/search/{attributeName}/{value} 	        I/O: --------- | ElementTO 
	 * ====================================== READ ME  =====================================================
	 */

	@LocalServerPort
	private int port;

	private String url;

	private RestTemplate restTemplate;
	//private ObjectMapper jsonMapper;

	@Autowired
	private ElementService elementService;

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
	//	this.jsonMapper = new ObjectMapper();
		this.url = "http://localhost:" + port + "/playground/elements";

	}

	@Before
	public void setup() throws InterruptedException {
		Location demo_entity_location = new Location(0,1);
		this.demo_entity = new ElementEntity(
				"playground_lazar", "0", demo_entity_location.getX(), demo_entity_location.getY()
				,"demo", new Date(), null, "demo type", null, "Aviv", "demo@gmail.com");
		/*
		 * Create numOfDemoEntities element entities more in array for more tests. we used sleep method
		 * for getting different time-stamps.
		 */
		Location demo_entities_locaiton = new Location();
		demo_entities = new ElementEntity[this.numOfEntities];
		for (int i = 0; i < this.numOfEntities; i++) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.demo_entities[i] = new ElementEntity(
					"playground_lazar", (i+1)+"", demo_entities_locaiton.getX(), demo_entities_locaiton.getY()
					,"demo", new Date(), null, "demo type", null, "Aviv", "demo@gmail.com");
		}
	}

	@After
	public void teardown() {
		this.elementService.cleanup();
	}



////////////////
// Feature 10 //
////////////////

// scenario 1
	@Test
	public void findElementByHisNameSuccessfulyInDatabaseWithOneElement() throws ElementAlreadyExistException {
// Given:
		this.elementService.addNewElement(this.demo_entity);

// When:
		String userPlayground = "playground_lazar";
		String email = "aviv@gmail.com";
		String attributeName = "name";
		String value = this.demo_entity.getName();

// Than:
		ElementTO[] allElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
				userPlayground, email, attributeName, value);

		boolean success = false;

		if (allElements.length == 1 && allElements[0].equals(new ElementTO(this.demo_entity)))
			success = true;

		assertTrue(success);
	}

// scenario 2
	@Test
	public void findElementByNameSuccessfulyInDatabaseWithTenElement() throws ElementAlreadyExistException {
		// Given: 10 element entities in database (and one is the target)
		ElementEntity demo_target = null;
		for (ElementEntity e : this.demo_entities) {
			if (Integer.parseInt(e.getId()) % 10 == 6) {
				e.setName("demo_target"); // the test
				demo_target = e;
			}
			this.elementService.addNewElement(e);
		}

		// When:
		String userPlayground = "playground_lazar";
		String email = "aviv@gmail.com";
		String attributeName = "name";
		String value = "demo_target";

		// Than:
		ElementTO[] allElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
				userPlayground, email, attributeName, value);

		boolean success = false;
		//System.err.println("Num of elements: " + allElements.length);

		if (allElements.length == 1 && allElements[0].equals(new ElementTO(demo_target)))
			success = true;

		assertTrue(success);
	}

// scenario 3
	@Test
	public void findFiveElementsByNameSuccessfulyInDatabaseWithTenElement() throws ElementAlreadyExistException {
		// Given: 10 element entities in database (which 5 of them are the targets)
		ArrayList<ElementEntity> demo_targets = new ArrayList<>();

		for (ElementEntity e : this.demo_entities) {
			if (Integer.parseInt(e.getId()) % 2 == 1) {
				e.setName("demo_target"); // the test
				demo_targets.add(e);
			}
			this.elementService.addNewElement(e);
		}
		demo_targets.trimToSize();

		// When:
		String userPlayground = "playground_lazar";
		String email = "aviv@gmail.com";
		String attributeName = "name";
		String value = "demo_target";

		// Than:
		ElementTO[] allElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
				userPlayground, email, attributeName, value);

		boolean success1 = false;
		boolean success2 = true;

		if (allElements.length == demo_targets.size()) {
			success1 = true;
			for (ElementEntity e : demo_targets) {
				if (!isContains(allElements, new ElementTO(e))) {
					success2 = false;
				}
			}
		}

		assertTrue(success1 & success2);
	}

	private boolean isContains(ElementTO[] allElements, ElementTO element) {
		for (ElementTO e : allElements) {
			if (e.equals(element))
				return true;
		}
		return false;
	}

// scenario 4
	@Test
	public void findElementFailedByInvalidAttributeNameInDatabaseWithOneElement() throws ElementAlreadyExistException {
		// Given:
		this.elementService.addNewElement(this.demo_entity);

		// When:
		String userPlayground = "playground_lazar";
		String email = "aviv@gmail.com";
		String attributeName = "attack";
		String value = "1";

		// Than:
		boolean success = false;

		try {
			ElementTO[] allElements = this.restTemplate.getForObject(
					this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
					userPlayground, email, attributeName, value);
		} catch (Exception e) { // TODO: replace with InvalidAttributeNameException
			success = true;
		}

		assertTrue(success);
	}

// scenario 5
	@Test
	public void findNoElementByTypeInDatabaseWithTenElement() throws ElementAlreadyExistException {

		// Given:
		ArrayList<ElementEntity> demo_targets = new ArrayList<>();

		for (ElementEntity e : this.demo_entities) {
			this.elementService.addNewElement(e);
		}

		demo_targets.trimToSize();

		// When:
		String userPlayground = "playground_lazar";
		String email = "aviv@gmail.com";
		String attributeName = "type";
		String value = "no demo type"; // no such value

		// Than:
		boolean success = false;

		ElementTO[] allElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class,
				userPlayground, email, attributeName, value);

		if (allElements.length == 0)
			success = true;

		assertTrue(success);
	}
	
	// TODO: Check pagination
}
