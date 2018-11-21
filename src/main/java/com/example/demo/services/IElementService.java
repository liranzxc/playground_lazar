package com.example.demo.services;

import java.util.List;

import com.example.demo.classes.EntityClasses.ElementEntity;

public interface IElementService {

	public void addNewElement(ElementEntity et);
	public void updateElement(ElementEntity et);
	public ElementEntity getElement(String playground, String id) throws Exception;
	
	public void deleteElement(String playground, String id) throws Exception;
	
	public List<ElementEntity> getAllElements();
	public List<ElementEntity> getElementsNearBy(double x, double y, double distance);

}
