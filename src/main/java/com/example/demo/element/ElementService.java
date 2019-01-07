package com.example.demo.element;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.demo.application.exceptions.InvalidPageRequestException;
import com.example.demo.application.exceptions.InvalidPageSizeRequestException;
import com.example.demo.element.exceptions.ElementAlreadyExistException;
import com.example.demo.element.exceptions.ElementNotFoundException;
import com.example.demo.element.exceptions.InvalidAttributeNameException;
import com.example.demo.element.exceptions.InvalidDistanceValueException;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotFoundException;

public interface ElementService{

	public void addNewElement(ElementEntity et, String email) throws ElementAlreadyExistException, InvalidRoleException;
	public void addElementFromOutside(ElementEntity et, String email) throws ElementAlreadyExistException, InvalidRoleException;
	
	public void updateElement(ElementEntity et, String email) throws ElementNotFoundException, InvalidRoleException;
	
	
	public ElementEntity getElement(String playground, String id, String email) throws ElementNotFoundException, InvalidRoleException, UserNotFoundException;
	
	public void deleteElement(String playground, String id, String email) throws InvalidRoleException;
	
	public List<ElementEntity> getAllElements();
	
	public List<ElementEntity> getAllElements(Pageable page, String email) throws InvalidPageSizeRequestException, InvalidPageRequestException, InvalidRoleException, UserNotFoundException;

	public List<ElementEntity> getAllElementsNearBy(double x, double y, double distance, String email ,Pageable page) throws InvalidDistanceValueException, InvalidRoleException, UserNotFoundException;

	public List<ElementEntity> getAllElementsByAttributeAndValue(String attribute, String value, String email, Pageable page) throws InvalidAttributeNameException, InvalidPageSizeRequestException, InvalidPageRequestException, InvalidRoleException, UserNotFoundException;
	
	public void cleanup();

}
