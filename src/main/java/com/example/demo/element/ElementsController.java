package com.example.demo.element;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.aop.UserExistInDB;
import com.example.demo.application.exceptions.InvalidPageRequestException;
import com.example.demo.application.exceptions.InvalidPageSizeRequestException;
import com.example.demo.element.exceptions.ElementAlreadyExistException;
import com.example.demo.element.exceptions.ElementNotFoundException;
import com.example.demo.element.exceptions.InvalidAttributeNameException;
import com.example.demo.element.exceptions.InvalidDistanceValueException;
import com.example.demo.user.exceptions.InvalidEmailException;

@RestController
@RequestMapping(path = "/playground/elements")
public class ElementsController {

	private ElementService elementService;
	
	
	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}

	/*
	 * Feature 5:
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
	 * Feature 6:
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
			throw new NullPointerException("cant update element into nothing");
		}
		element.setCreatorEmail(email);
		element.setCreatorPlayground(userPlayground);

		ElementEntity entity = element.ToEntity();
		this.elementService.updateElement(entity);

	}

	/*
	 * Feature 7:
	 */
	@UserExistInDB
	@RequestMapping(path = {
			"/{userPlayground}/{email}/{playground}/{id}" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO getElement(@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email, @PathVariable("playground") String playground,
			@PathVariable("id") String id) throws ElementNotFoundException {
		return new ElementTO(this.elementService.getElement(playground, id));
	}

	

	/*
	 * Feature 8:
	 */
	@RequestMapping(path = {
			"/{userPlayground}/{email}/all" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getAllElements(@PathVariable(name = "userPlayground") String userPlayground,
			@PathVariable(name = "email") String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page)
			throws InvalidPageSizeRequestException, InvalidPageRequestException {

		List<ElementEntity> mylist = elementService.getAllElementsManager(PageRequest.of(page, size,Sort.by("id")));

		return mylist.stream().map(ElementTO::new).collect(Collectors.toList()).toArray(new ElementTO[0]);
	}

	/*
	 * Feature 9:
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

		List<ElementEntity> nearBy = this.elementService.getAllElementsNearByManager(x, y, distance,
				PageRequest.of(page, size,Sort.by("id")));

		return nearBy.stream().map(ElementTO::new).collect(Collectors.toList()).toArray(new ElementTO[nearBy.size()]);

	}


	/*
	 * Feature 10:
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
		System.err.println("attribute name = " + attributeName + " value =  " + value);
		return this.elementService.getAllElementsByAttributeAndValueManager(attributeName, value, PageRequest.of(page, size,Sort.by("id")))
				.stream().map(ElementTO::new).collect(Collectors.toList()).toArray(new ElementTO[0]);

	}
	
	// this method checks if there is a '@' in the name and there is something
	// before it and after it
	private boolean verifiedEmail(String email) {
		String[] emailParts = email.split("@");

		if (emailParts.length != 2 || emailParts[0].length() < 3) {
			return false;
		}

		String[] emailTailParts = emailParts[1].split("\\.");

		if (emailTailParts.length < 2 || emailTailParts[0].isEmpty() || emailTailParts[1].isEmpty()) {
			return false;
		}

		return true;
	}

}
