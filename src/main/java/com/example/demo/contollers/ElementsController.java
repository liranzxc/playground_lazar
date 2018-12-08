package com.example.demo.contollers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.classes.Location;
import com.example.demo.classes.entities.ElementEntity;
import com.example.demo.classes.exceptions.InvalidEmailException;
import com.example.demo.classes.exceptions.InvalidPageRequestException;
import com.example.demo.classes.exceptions.InvalidPageSizeRequestException;
import com.example.demo.classes.to.ElementTO;
import com.example.demo.services.elementservices.IElementService;
import com.example.demo.classes.exceptions.ElementNotFoundException;
import com.example.demo.classes.exceptions.InvalidAttributeNameException;
import com.example.demo.classes.exceptions.InvalidDistanceValueException;
import com.example.demo.classes.exceptions.ElementAlreadyExistException;

@RestController
@RequestMapping(path = "/playground/elements")
public class ElementsController {

	@Autowired
	private IElementService elementService;

	// TODO this public static final is giving an error when uploading server
	// Test element
	// public static final ElementTO DEMO_ELEMENT_TO =
	// new ElementTO("playground_lazar", "1", new Location(0, 0), "Demo", new
	// Date("12.03.18"), null, "TEST", new TreeMap<String,Object>(), "playGround",
	// "demp777@gmail.com");

	/*
	 * Feature 6:
	 */
	@RequestMapping(path = {
			"/{userPlayground}/{email}" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO createElement(@RequestBody ElementTO element,
			@PathVariable(name = "userPlayground", required = true) String userPlayground,
			@PathVariable(name = "email", required = true) String email)
			throws InvalidEmailException, ElementAlreadyExistException {

		if (!verifiedEmail(email)) {
			System.err.println("create element throwing Exception");
			throw new InvalidEmailException("Illeagal email has been given");
		}

		this.elementService.addNewElement(element.ToEntity());

		return element;
	}

	/*
	 * Feature 7:
	 */
	@RequestMapping(path = {
			"/{userPlayground}/{email}/{playground}/{id}" }, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateElement(@RequestBody ElementTO element,
			@PathVariable(name = "userPlayground", required = true) String userPlayground,
			@PathVariable(name = "email", required = true) String email,
			@PathVariable(name = "playground", required = true) String playground,
			@PathVariable(name = "id", required = true) String id)
			throws NullPointerException, ElementNotFoundException {

		if (element == null) {
			throw new NullPointerException("cant update element into nothing"); // TODO maybe changed exception thrown
		}
		element.setCreatorEmail(email);
		element.setCreatorPlayground(userPlayground);

		ElementEntity entity = element.ToEntity();
		this.elementService.updateElement(entity);

	}

	/*
	 * Feature 8:
	 */
	@RequestMapping(path = {
			"/{userPlayground}/{email}/{playground}/{id}" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO getElement(@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email, @PathVariable("playground") String playground,
			@PathVariable("id") String id) throws ElementNotFoundException {
		// TODO add pagination
		return new ElementTO(this.elementService.getElement(playground, id));
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
	 * Feature 9:
	 */
	@RequestMapping(path = {
			"/{userPlayground}/{email}/all" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getAllElements(@PathVariable(name = "userPlayground") String userPlayground,
			@PathVariable(name = "email") String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page)
			throws InvalidPageSizeRequestException, InvalidPageRequestException {

		List<ElementEntity> mylist = elementService.getAllElements(PageRequest.of(page, size,Sort.by("id")));

		return mylist.stream().map(ElementTO::new).collect(Collectors.toList()).toArray(new ElementTO[0]);

		// old code .. better code up - liran nachman

		/*
		 * ArrayList<ElementTO> result = new ArrayList<>(mylist.size()); for
		 * (ElementEntity elementEntity : elementService.getAllElements()) {
		 * result.add(new ElementTO(elementEntity)); }
		 * 
		 * return result.toArray(new ElementTO[result.size()]); // Transfer to array //
		 * TODO: pagination
		 */ }

	/*
	 * Feature 10:
	 */
	@RequestMapping(path = {
			"/{userPlayground}/{email}/near/{x}/{y}/{distance}" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getAllElementsNearToPlayaer(@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email, @PathVariable("x") double x, @PathVariable("y") double y,
			@PathVariable("distance") double distance,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page)
			throws InvalidDistanceValueException, InvalidPageSizeRequestException, InvalidPageRequestException {

		if (distance < 0.0)
			throw new InvalidDistanceValueException("Distance must be equal or higher from 0");

//		List<ElementEntity> allElementEntities = elementService.getAllElements(size, page);
//		
//		ArrayList<ElementEntity> allNearElementEntities = 
//				allElementEntities.stream()  // transfer 
//				.filter(  // filter by predicate
//						e -> getDistanceBetween(e.getLocation(), new Location(x, y)) <= distance)
//				.collect(Collectors.toCollection(ArrayList::new));  // transfer to array
//		
//		allNearElementEntities.trimToSize();
//		
//		ElementTO[] allNearElementsTO = new ElementTO[allNearElementEntities.size()];
//		for(int i = 0; i < allNearElementsTO.length; i++) {
//			allNearElementsTO[i] = new ElementTO(allNearElementEntities.get(i));
//		}
//		
		List<ElementEntity> nearBy = this.elementService.getAllElementsNearBy(x, y, distance,
				PageRequest.of(page, size,Sort.by("id")));

		return nearBy.stream().map(ElementTO::new).collect(Collectors.toList()).toArray(new ElementTO[nearBy.size()]);

	}

	private double getDistanceBetween(Location l1, Location l2) {
		return Math.sqrt(Math.pow(l1.getX() - l2.getX(), 2) + Math.pow(l1.getY() - l2.getY(), 2));
	}

	/*
	 * Feature 11:
	 */
	@RequestMapping(path = {
			"/{userPlayground}/{email}/search/{attributeName}/{value}" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] searchAllElementsWithSameAttributeAndValue(@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email, @PathVariable("attributeName") String attributeName,
			@PathVariable("value") String value,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page)
			throws InvalidAttributeNameException, InvalidEmailException, InvalidPageSizeRequestException,
			InvalidPageRequestException {

		if (!verifiedEmail(email)) {
			throw new InvalidEmailException();
		}
		if (size < 1) {
			throw new InvalidPageSizeRequestException("Size of a page must be at least 1");
		} else if (page < 0) {
			throw new InvalidPageRequestException("Page index must be at least 0");
		}

		
		/// better
		return this.elementService.getAllElementsByAttributeAndValue(attributeName, value, PageRequest.of(page, size,Sort.by("id")))
				.stream().map(ElementTO::new).collect(Collectors.toList()).toArray(new ElementTO[0]);

	}

}
