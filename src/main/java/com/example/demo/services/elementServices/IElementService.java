package com.example.demo.services.elementServices;

import java.util.List;

import com.example.demo.classes.EntityClasses.ElementEntity;
import com.example.demo.classes.exceptions.ElementAlreadyExistException;
import com.example.demo.classes.exceptions.ElementNotFoundException;
import com.example.demo.classes.exceptions.InvalidAttributeNameException;
import com.example.demo.classes.exceptions.InvalidDistanceValueException;
import com.example.demo.classes.exceptions.InvalidPageRequestException;
import com.example.demo.classes.exceptions.InvalidPageSizeRequestException;

public interface IElementService{

	public void addNewElement(ElementEntity et) throws ElementAlreadyExistException;
	public void updateElement(ElementEntity et) throws ElementNotFoundException;
	public ElementEntity getElement(String playground, String id) throws ElementNotFoundException;
	
	public void deleteElement(String playground, String id);
	
	public List<ElementEntity> getAllElements();
	public List<ElementEntity> getAllElements(int size, int page) throws InvalidPageSizeRequestException, InvalidPageRequestException;
	public List<ElementEntity> getAllElementsNearBy(double x, double y, double distance) throws InvalidDistanceValueException;
	public List<ElementEntity> getAllElementsByAttributeAndValue(String attribute, String value) throws InvalidAttributeNameException;
	
	public void cleanup();

}
