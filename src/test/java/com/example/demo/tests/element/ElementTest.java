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
import org.springframework.data.mongodb.core.aggregation.SetOperators.AllElementsTrue;
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

	private int numOfDemoEntities = 50;
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

		System.err.println(this.url);

	}

	@Before
	public void setup() throws InterruptedException {

		ElementEntity.zeroID();
		this.demo_entity = new ElementEntity("playground_lazar", new Location(0, 1), "demo", new Date(), null,
				"demo type", null, "Aviv", "demo@gmail.com");

		/*
		 * Create 11 element entities more in array for more tests. we used sleep method
		 * for getting different time-stamps.
		 */
		demo_entities = new ElementEntity[numOfDemoEntities];
		for (int i = 0; i < this.numOfDemoEntities; i++) {
			Thread.sleep(20);
			this.demo_entities[i] = new ElementEntity("playground_lazar"+i, new Location(), "demo",
					new Date(), null, "demo type", null, "Aviv", "demo@gmail.com");
		}

	}

	@After
	public void teardown() {
		this.elementService.cleanup();
	}

	///////////////
	// Feature 6 //
	///////////////

	@Test
	public void createElementSuccsefully() {
		String usrPlayground = "lazar_playground";
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

	@Test(expected = ElementAlreadyExistException.class)
	public void createElementWhenElementAlreadyExist() throws ElementAlreadyExistException {
		// given
		this.elementService.addNewElement(demo_entity);

		// when
		String usrPlayground = "lazar_playground";
		String email = "demo@gmail.com";
		ElementTO eto = new ElementTO(demo_entity);

		try {
			this.restTemplate.postForObject(this.url + "/{userPlayground}/{email}", eto, ElementTO.class, usrPlayground,
					email);

		} catch (Exception e) {
			throw new ElementAlreadyExistException();
		}
	}

	@Test(expected = InvalidEmailException.class)
	public void createElementWithInvalidEmailAndFail() throws InvalidEmailException {
		String usrPlayground = "lazar_playground";
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

	///////////////
	// Feature 7 //
	///////////////

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
	// Feature 8 //
	///////////////

	@Test
	public void getSpecificElementSuccess() throws ElementNotFoundException, ElementAlreadyExistException {
		// given
		ElementTO originalElementTO = new ElementTO(demo_entity);
		String userPlayground = "lazar_playground";
		String email = "demo@gmail.com";
		String playground = originalElementTO.getPlayground();
		String id = originalElementTO.getId();

		this.elementService.addNewElement(originalElementTO.ToEntity());

		ElementTO elementTOFromDB;
		elementTOFromDB = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/{playground}/{id}",
				ElementTO.class, userPlayground, email, playground, id);

		// that
		boolean success = true;
		if (!elementTOFromDB.getId().equals(originalElementTO.getId())) {
			success = false;
		} else if (!elementTOFromDB.getPlayground().equals(originalElementTO.getPlayground())) {
			success = false;
		}

		assertTrue(success);
	}

	@Test(expected = ElementNotFoundException.class)
	public void getSpecificElementFail() throws ElementNotFoundException {
		// given element not in database (tearDown and setup take care of that)

		String userPlayground = "lazar_playground";
		String email = "demo@gmail.com";
		String playground = "lazar_playground";
		String id = "1";

		try {
			this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/{playground}/{id}", ElementTO.class,
					userPlayground, email, playground, id);
		} catch (Exception e) {
			throw new ElementNotFoundException("element doesnt exist");
		}
	}

	///////////////
	// Feature 9 //
	///////////////

	// scenario 1:
	@Test
	public void GetAllElementsSuccessWithOneElement() throws ElementAlreadyExistException {
		// Given:
		this.elementService.addNewElement(this.demo_entity);

		// When:
		String userPlayground = "playground_lazar";
		String email = "aviv@gmail.com";

		ElementTO[] allElements = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/all",
				ElementTO[].class, userPlayground, email);

		// Than:
		boolean success = false;
		if (allElements.length == 1 && allElements[0].equals(new ElementTO(this.demo_entity)))
			success = true;

		assertTrue(success);
	}

	// TODO: test for pagination - feature 9

	//////////////////////////////////////////////////
	/////////////////// Feature 10 ///////////////////
	//////////////////////////////////////////////////

	// scenario 1
	@Test
	public void GetOneElementSuccessfulyInDistanceOne() throws ElementAlreadyExistException {

		// Given:
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

		// Given:
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

		// this.elementService.getAllElementsNearBy(x, y, distance, 10, 1);

		assertTrue(success);
	}

	// scenario 3
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

		// Given:
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

		allElements = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/near/{x}/{y}/{distance}",
				ElementTO[].class, userPlayground, email, x, y, distance);

		if (allElements.length == 10)
			success = true;

		assertTrue(success);
	}

	@Test
	public void LiranTestPagantion() throws ElementAlreadyExistException, InvalidPageSizeRequestException, InvalidPageRequestException {
		

		for(int i= 0 ; i< 30;i++)
		{
			this.elementService.addNewElement(demo_entities[i]);
		}
		
		List<ElementEntity> list = this.elementService.getAllElements(PageRequest.of(1, 3, Sort.by("id")));
		
		list.forEach(System.err::println);
		
		
		

	}
}
