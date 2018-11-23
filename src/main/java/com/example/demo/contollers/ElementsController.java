package com.example.demo.contollers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.classes.Location;

import com.example.demo.classes.exceptions.InvalidEmailException;
import com.example.demo.classes.exceptions.elementAlreadyExistException;
import com.example.demo.classes.EntityClasses.ElementEntity;

import com.example.demo.classes.ToClasses.ElementTO;
import com.example.demo.services.IElementService;

@RestController
@RequestMapping(path = "/playground/elements")
public class ElementsController {
	
	@Autowired
	private IElementService elementService;
	
	//TODO this public static final is giving an error when uploading server 
	//Test element
	//public static final ElementTO DEMO_ELEMENT_TO = 
	//		new ElementTO("playground_lazar", "1", new Location(0, 0), "Demo", new Date("12.03.18"), null, "TEST", new TreeMap<String,Object>(), "playGround", "demp777@gmail.com");

	/*
	 * Feature 5:
	 */
	@RequestMapping(
			path = {"/{userPlayground}/{email}" }, 
			method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO createElement(@RequestBody ElementTO element,
			@PathVariable(name = "userPlayground", required = true) String userPlayground,
			@PathVariable(name = "email", required = true) String email) throws InvalidEmailException, elementAlreadyExistException, InvalidEmailException, com.example.demo.classes.exceptions.elementAlreadyExistException {
			

		
		if (!verifiedEmail(email)) {
			System.err.println("create element throwing Exception");
			throw new InvalidEmailException("Illeagal email has been given");
		}

		

		this.elementService.addNewElement(element.ToEntity());

		return element;
	}

	/*
	 * Feature 6:
	 */
	@RequestMapping(
			path = {"/{userPlayground}/{email}/{playground}/{id}" }, 
			method = RequestMethod.PUT, 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateElement(@RequestBody ElementTO element,
			@PathVariable(name = "userPlayground", required = true) String userPlayground,
			@PathVariable(name = "email", required = true) String email,
			@PathVariable(name = "playground", required = true) String playground,
			@PathVariable(name = "id", required = true) String id) throws Exception {

		if (element == null) {
			throw new Exception(); // TODO maybe changed exception thrown
		}
		
		ElementEntity entity = element.ToEntity();
		this.elementService.addNewElement(entity);

		// TODO do some valid checks on new element and update dataBase with new element
	}

	/*
	 * Feature 7:
	 */
	@RequestMapping(
			path = {"/{userPlayground}/{email}/{playground}/{id}" }, 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO getElement(
			@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email, 
			@PathVariable("playground") String playground,
			@PathVariable("id") String id) throws Exception {

		// TODO extract element from data bases
		ElementTO element = new ElementTO();
		element.setCreationDate(new Date());
		element.setPlayground(userPlayground);
		element.setCreatorPlayground(userPlayground);
		element.setCreatorEmail(email);
		element.setLocation(new Location(0, 0));
		element.setName("Shay");
		element.setType("TEST");
		element.setAttributes(new HashMap<String, Object>());

// 		//TODO throw exception if element hasn't been found in dataBase
//		if(element == null){
//			throw new Exception(); //TODO maybe changed exception thrown
//		}
		
		return element;
	}

	// this method checks if there is a '@' in the name and there is something
	// before it and after it
	private boolean verifiedEmail(String email) {
		if (email.length() < 3) {
			return false;
		}

		// iterate from (1- (n-1)) letters, no need the first and last chars, '@' should
		// be in the middle
		for (int i = 1; i < email.length() - 1; i++) {
			if (email.charAt(i) == '@') {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Feature 8:
	 */
	@RequestMapping(
			path = { "/{userPlayground}/{email}/all" },
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getAllElements(
			@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email) {
		
		return createRandomArrayOfElementTO();
	}
	
	/*
	 * Feature 9:
	 */
	@RequestMapping(
			path = { "/{userPlayground}/{email}/near/{x}/{y}/{distance}" },
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getAllElementsNearToPlayaer(
			@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email,
			@PathVariable("x") double x,
			@PathVariable("y") double y,
			@PathVariable("distance") double distance) {
		
		ElementTO[] elementsTO = createRandomArrayOfElementTO();
		
		return (ElementTO[]) Arrays.asList(elementsTO).stream()
			.filter(e -> getDistanceBetween(e.getLocation(), new Location(x, y)) 
					<= distance)
			.toArray();
	}
	
	
	private double getDistanceBetween(Location l1, Location l2) {
		return Math.sqrt(Math.pow(l1.getX() - l2.getX(), 2)
				+ Math.pow(l1.getY() - l2.getY(), 2));
	}

	/*
	 * Feature 10:
	 */
	@RequestMapping(
			path = { "/{userPlayground}/{email}/search/{attributeName}/{value}" },
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] searchAllElementsWithSameAttributeAndValue(
			@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email,
			@PathVariable("attributeName") String attributeName,
			@PathVariable("value") double value) {
		
		
		ElementTO[] elementsDataBase = createRandomArrayOfElementTO();
		// TODO: ask eyal	
		//return allElements;
		return null;
	}
	
	
	private ElementTO[] createRandomArrayOfElementTO() {
		ArrayList<ElementTO> elementsTO = new ArrayList<>();
		
		// create attributes:
		Map<String, Object> attributesMap1 = 
				new HashMap<>();
		attributesMap1.put("1", new Object());
		attributesMap1.put("2", new Object());
		attributesMap1.put("3", new Object());
		
		Map<String, Object> attributesMap2 = 
				new HashMap<>();
		attributesMap1.put("4", new Object());
		attributesMap1.put("5", new Object());
		attributesMap1.put("6", new Object());
		attributesMap1.put("7", new Object());
		
		Map<String, Object> attributesMap3 = 
				new HashMap<>();
		attributesMap1.put("8", new Object());
		attributesMap1.put("9", new Object());
		
		Map<String, Object> attributesMap4 = 
				new HashMap<>();
		attributesMap1.put("10", new Object());
		attributesMap1.put("11", new Object());
		
		
		// create Elements
		ElementTO e1 = new ElementTO("P1", "123", 
				new Location(1, 2), "Tirex", new Date(), null, "A", 
				attributesMap1, "P2", "Demo.co.il");
		ElementTO e2 = new ElementTO("P2", "1234", 
				new Location(1, 2), "Polo", new Date(), null, "B", 
				attributesMap2, "P5", "Demo.co.il");
		ElementTO e3 = new ElementTO("P3", "12345", 
				new Location(1, 2), "Dindin", new Date(), null, "C", 
				attributesMap3, "P3", "Demo.co.il");
		ElementTO e4 = new ElementTO("P4", "1237", 
				new Location(1, 2), "Riko", new Date(), null, "D", 
				attributesMap4, "P4", "Demo.co.il");
		
		
		elementsTO.addAll(Arrays.asList(e1, e2, e3, e4));
		
		return (ElementTO[]) elementsTO.toArray();
	}

}
