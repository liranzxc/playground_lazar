package com.example.demo.services;

import java.util.List;

import com.example.demo.classes.EntityClasses.ElementEntity;
import com.example.demo.classes.exceptions.ElementAlreadyExistException;
import com.example.demo.classes.exceptions.ElementNotFoundException;

public interface IElementService {

	public void addNewElement(ElementEntity et) throws ElementAlreadyExistException;
	public void updateElement(ElementEntity et);
	public ElementEntity getElement(String playground, String id) throws ElementNotFoundException;
	
	public void deleteElement(String playground, String id) throws Exception;
	
	public List<ElementEntity> getAllElements();
	public List<ElementEntity> getElementsNearBy(double x, double y, double distance);
	public void cleanup();

}
