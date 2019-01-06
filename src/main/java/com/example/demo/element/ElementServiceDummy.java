package com.example.demo.element;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.data.domain.Pageable;

import com.example.demo.application.exceptions.InvalidPageRequestException;
import com.example.demo.application.exceptions.InvalidPageSizeRequestException;
import com.example.demo.element.exceptions.ElementAlreadyExistException;
import com.example.demo.element.exceptions.ElementNotFoundException;
import com.example.demo.element.exceptions.InvalidAttributeNameException;
import com.example.demo.element.exceptions.InvalidDistanceValueException;
import com.example.demo.user.exceptions.InvalidRoleException;

//@Service


/// don't use Dummy Service anymore !!
public class ElementServiceDummy implements ElementService {
	
	private Map<String, ElementEntity> entities;

	@Override
	public void addNewElement(ElementEntity et, String email)
			throws ElementAlreadyExistException, InvalidRoleException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addElementFromOutside(ElementEntity et, String email)
			throws ElementAlreadyExistException, InvalidRoleException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateElement(ElementEntity et, String email) throws ElementNotFoundException, InvalidRoleException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ElementEntity getElement(String playground, String id, String email)
			throws ElementNotFoundException, InvalidRoleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteElement(String playground, String id, String email) throws InvalidRoleException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ElementEntity> getAllElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementEntity> getAllElements(Pageable page, String email)
			throws InvalidPageSizeRequestException, InvalidPageRequestException, InvalidRoleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementEntity> getAllElementsNearBy(double x, double y, double distance, String email, Pageable page)
			throws InvalidDistanceValueException, InvalidRoleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementEntity> getAllElementsByAttributeAndValue(String attribute, String value, String email,
			Pageable page) throws InvalidAttributeNameException, InvalidPageSizeRequestException,
			InvalidPageRequestException, InvalidRoleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}


}
