package com.example.demo.element;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.exceptions.InvalidPageRequestException;
import com.example.demo.application.exceptions.InvalidPageSizeRequestException;
import com.example.demo.element.exceptions.ElementAlreadyExistException;
import com.example.demo.element.exceptions.ElementNotFoundException;
import com.example.demo.element.exceptions.InvalidAttributeNameException;
import com.example.demo.element.exceptions.InvalidDistanceValueException;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotFoundException;

@RestController
@RequestMapping(path = "/playground/elements")
public class ElementsController {

	private ElementService elementService;
	
	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}
	

	@RequestMapping(path = {
			"/{userPlayground}/{email}" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO createElement(@RequestBody ElementTO element,
			@PathVariable(name = "userPlayground", required = true) String userPlayground,
			@PathVariable(name = "email", required = true) String email)
			throws InvalidEmailException, ElementAlreadyExistException, InvalidRoleException {

	
		this.elementService.addNewElement(element.ToEntity(), email);

		return element;
	}

	
	@RequestMapping(path = {
			"/{userPlayground}/{email}/{playground}/{id}" }, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateElement(@RequestBody ElementTO element,
			@PathVariable(name = "userPlayground", required = true) String userPlayground,
			@PathVariable(name = "email", required = true) String email,
			@PathVariable(name = "playground", required = true) String playground,
			@PathVariable(name = "id", required = true) String id)
			throws NullPointerException, ElementNotFoundException, InvalidRoleException {
		
		if (element == null) {
			throw new NullPointerException("cant update element into nothing");
		}


		ElementEntity entity = element.ToEntity();
		this.elementService.updateElement(entity, email);
	}

	
	@RequestMapping(path = {
			"/{userPlayground}/{email}/{playground}/{id}" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO getElement(@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email, @PathVariable("playground") String playground,
			@PathVariable("id") String id) throws ElementNotFoundException, InvalidRoleException, UserNotFoundException {

		return new ElementTO(this.elementService.getElement(playground, id, email));
	}	

	
	@RequestMapping(path = {
			"/{userPlayground}/{email}/all" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getAllElements(@PathVariable(name = "userPlayground") String userPlayground,
			@PathVariable(name = "email") String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page)
			throws InvalidPageSizeRequestException, InvalidPageRequestException, InvalidRoleException, UserNotFoundException {

		List<ElementEntity> mylist = this.elementService.getAllElements(PageRequest.of(page, size), email);	
		return mylist.stream().map(ElementTO::new).collect(Collectors.toList()).toArray(new ElementTO[0]);
	}

	@RequestMapping(path = {
			"/{userPlayground}/{email}/near/{x}/{y}/{distance}" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getAllElementsNearToPlayaer(@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email, @PathVariable("x") double x, @PathVariable("y") double y,
			@PathVariable("distance") double distance,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page)
			throws InvalidDistanceValueException, InvalidPageSizeRequestException, 
			InvalidPageRequestException, InvalidRoleException, UserNotFoundException {

		List<ElementEntity> nearBy = this.elementService.getAllElementsNearBy(x, y, distance, email, PageRequest.of(page, size));
		return nearBy.stream().map(ElementTO::new).collect(Collectors.toList()).toArray(new ElementTO[nearBy.size()]);

	}


	@RequestMapping(path = {
			"/{userPlayground}/{email}/search/{attributeName}/{value}" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] searchAllElementsWithSameAttributeAndValue(@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email, @PathVariable("attributeName") String attributeName,
			@PathVariable("value") String value,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page)
			throws InvalidAttributeNameException, InvalidEmailException, InvalidPageSizeRequestException,
			InvalidPageRequestException, InvalidRoleException, UserNotFoundException {
		
	
		List<ElementEntity> entities = this.elementService.getAllElementsByAttributeAndValue(attributeName, value, email, PageRequest.of(page, size));
		
		return entities
			.stream()
			.map(ElementTO::new)
			.collect(Collectors.toList())
			.toArray(new ElementTO[0]);
	}
}
