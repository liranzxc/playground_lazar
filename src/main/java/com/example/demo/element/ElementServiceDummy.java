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

//@Service


/// don't use Dummy Service anymore !!
public class ElementServiceDummy implements ElementService {
	
	private Map<String, ElementEntity> entities;
	
	@PostConstruct
	public void init() {
		this.entities = new ConcurrentHashMap<>();
		// maybe add details ?? 
		
	}

	@Override
	public void addNewElement(ElementEntity et) throws ElementAlreadyExistException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateElement(ElementEntity et) throws ElementNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ElementEntity getElementPlayer(String playground, String id) throws ElementNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementEntity getElementManager(String playground, String id) throws ElementNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteElement(String playground, String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ElementEntity> getAllElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementEntity> getAllElementsPlayer(Pageable page)
			throws InvalidPageSizeRequestException, InvalidPageRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementEntity> getAllElementsManager(Pageable page)
			throws InvalidPageSizeRequestException, InvalidPageRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementEntity> getAllElementsNearByPlayer(double x, double y, double distance, Pageable page)
			throws InvalidDistanceValueException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementEntity> getAllElementsNearByManager(double x, double y, double distance, Pageable page)
			throws InvalidDistanceValueException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementEntity> getAllElementsByAttributeAndValuePlayer(String attribute, String value, Pageable page)
			throws InvalidAttributeNameException, InvalidPageSizeRequestException, InvalidPageRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementEntity> getAllElementsByAttributeAndValueManager(String attribute, String value, Pageable page)
			throws InvalidAttributeNameException, InvalidPageSizeRequestException, InvalidPageRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addElementFromOutside(ElementEntity demo_entity) throws ElementAlreadyExistException {
		// TODO Auto-generated method stub
		
	}



}
