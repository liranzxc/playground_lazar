package com.example.demo.contollers;

import java.util.Date;
import java.util.TreeMap;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.classes.ElementTO;
import com.example.demo.classes.Location;

@RestController
@RequestMapping(path = "/playground/elements")
public class ElementsController {
	
	public static final ElementTO DEMO_ELEMENT_TO = 
			new ElementTO("playground_lazar", "1", new Location(0, 0), "Demo", new Date("12.03.18"), null, "TEST", new TreeMap<String,Object>(), "playGround", "demp777@gmail.com");

	/*
	 * Feature 5:
	 */
	@RequestMapping(
			path = {"/{userPlayerground}/{email}" }, 
			method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO createElement(@RequestBody ElementTO element,
			@PathVariable(name = "userPlayground", required = true) String userPlayground,
			@PathVariable(name = "email", required = true) String email) throws Exception {

		if (!verifiedEmail(element.getCreatorEmail())) {
			throw new Exception(); // TODO maybe changed exception thrown
		}

		element.setCreationDate(new Date()); // TODO check if new Date gives now
		element.setExpireDate(null);// is null ok to represent never?
		// TODO maybe set more stuffs

		return element;
	}

	/*
	 * Feature 6:
	 */
	@RequestMapping(
			path = {"/{userPlayerground}/{email}/{playground}/{id}" }, 
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

		// TODO do some valid checks on new element and update dataBase with new element
	}

	/*
	 * Feature 7:
	 */
	@RequestMapping(
			path = {"/{userPlayerground}/{email}/{playground}/{id}" }, 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO getElement(
			@PathVariable("userPlayerground") String userPlayground,
			@PathVariable("email") String email, 
			@PathVariable("playground") String playground,
			@PathVariable("id") String id) throws Exception {

		System.out.println("ups: " + userPlayground + " email: " + email + " pg: " + playground + " id: " + id);

		System.out.println("enterd get method");
		// TODO extract element from data bases
		ElementTO eto = new ElementTO("TEST", "TEST", null, "TEST", null, null, "TEST", null, "TEST", "TEST");

		// TODO throw exception if element hasn't been found in dataBase
		// if(eto == null){
		// throw new Exception(); //TODO maybe changed exception thrown
		// }
		return eto;
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
			@PathVariable("userPlayerground") String userPlayground,
			@PathVariable("email") String email) {
		
		ElementTO[] allElements = new ElementTO[1];
		allElements[0] = DEMO_ELEMENT_TO;
		return allElements;
	}
	
	/*
	 * Feature 9:
	 */
	@RequestMapping(
			path = { "/{userPlayground}/{email}/near/{x}/{y}/{distance}" },
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getAllElementsNearToPlayaer(
			@PathVariable("userPlayerground") String userPlayground,
			@PathVariable("email") String email,
			@PathVariable("x") double x,
			@PathVariable("y") double y,
			@PathVariable("distance") double distance) {
		
		ElementTO[] allElements = new ElementTO[1];
		allElements[0] = DEMO_ELEMENT_TO;
		return allElements;
	}
	
	/*
	 * Feature 10:
	 */
	@RequestMapping(
			path = { "/{userPlayground}/{email}/search/{attributeName}/{value}" },
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] searchAllElementswithSameAttributeAndValue(
			@PathVariable("userPlayerground") String userPlayground,
			@PathVariable("email") String email,
			@PathVariable("attributeName") String attributeName,
			@PathVariable("value") double value) {
		
		ElementTO[] allElements = new ElementTO[1];
		allElements[0] = DEMO_ELEMENT_TO;
		return allElements;
	}

}
