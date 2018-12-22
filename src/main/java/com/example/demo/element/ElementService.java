package com.example.demo.element;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.demo.application.exceptions.InvalidPageRequestException;
import com.example.demo.application.exceptions.InvalidPageSizeRequestException;
import com.example.demo.element.exceptions.ElementAlreadyExistException;
import com.example.demo.element.exceptions.ElementNotFoundException;
import com.example.demo.element.exceptions.InvalidAttributeNameException;
import com.example.demo.element.exceptions.InvalidDistanceValueException;

public interface ElementService{

	public void addNewElement(ElementEntity et) throws ElementAlreadyExistException;
	public void updateElement(ElementEntity et) throws ElementNotFoundException;
	public ElementEntity getElement(String playground, String id) throws ElementNotFoundException;
	
	public void deleteElement(String playground, String id);
	
	public List<ElementEntity> getAllElements();
	public List<ElementEntity> getAllElements(Pageable page) throws InvalidPageSizeRequestException, InvalidPageRequestException;
	public List<ElementEntity> getAllElementsNearBy(double x, double y, double distance, Pageable page) throws InvalidDistanceValueException;
	public List<ElementEntity> getAllElementsByAttributeAndValue(String attribute, String value,Pageable page) throws InvalidAttributeNameException;
	
	public void cleanup();
	
	// Special for Gherkin Test
	public void addElementFromOutside(ElementEntity demo_entity) throws ElementAlreadyExistException;

}
