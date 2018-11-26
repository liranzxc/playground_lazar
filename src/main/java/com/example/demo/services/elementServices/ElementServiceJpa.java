package com.example.demo.services.elementServices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.Repository.IElementRepository;
import com.example.demo.classes.EntityClasses.ElementEntity;
import com.example.demo.classes.exceptions.ElementAlreadyExistException;
import com.example.demo.classes.exceptions.ElementNotFoundException;
import com.example.demo.classes.exceptions.InvalidAttributeNameException;
import com.example.demo.classes.exceptions.InvalidDistanceValueException;

public class ElementServiceJpa implements IElementService {

	@Autowired
	private IElementRepository dateBase;
	
	
	@Override
	public void addNewElement(ElementEntity et) throws ElementAlreadyExistException {
		
	}

	@Override
	public void updateElement(ElementEntity et) throws ElementNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ElementEntity getElement(String playground, String id) throws ElementNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteElement(String playground, String id) throws ElementNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ElementEntity> getAllElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementEntity> getAllElements(int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementEntity> getAllElementsNearBy(double x, double y, double distance)
			throws InvalidDistanceValueException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementEntity> getAllElementsByAttributeAndValue(String attribute, String value)
			throws InvalidAttributeNameException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

}
