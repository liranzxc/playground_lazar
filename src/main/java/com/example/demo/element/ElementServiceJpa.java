package com.example.demo.element;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.aop.MyLog;
import com.example.demo.application.exceptions.InvalidPageRequestException;
import com.example.demo.application.exceptions.InvalidPageSizeRequestException;
import com.example.demo.element.exceptions.ElementAlreadyExistException;
import com.example.demo.element.exceptions.ElementNotFoundException;
import com.example.demo.element.exceptions.InvalidAttributeNameException;
import com.example.demo.element.exceptions.InvalidDistanceValueException;

@Service
public class ElementServiceJpa implements ElementService {

	private static int ID = 0;
	
	private ElementRepository dataBase;
	
	
	@Autowired
	public void setDataBase(ElementRepository dataBase) {
		this.dataBase = dataBase;
	}

	@Override
	@Transactional
	@MyLog 
	public void addNewElement(ElementEntity et) throws ElementAlreadyExistException{
		
		int newID = ++ID;
		String key = ElementEntity.createKeyFromIdAndPlayground(newID+"", et.getPlayground());
		System.err.println("inside createElement Service setting key to:  = " + key);
		et.setKey(key);
		
		if (!this.dataBase.existsByKey(key)) {
			this.dataBase.save(et);
		} else {
			ID--; // reverse assigned ID
			throw new ElementAlreadyExistException();
		}
	}
	
	
	@Override
	@Transactional
	@MyLog 
	public void addElementFromOutside(ElementEntity et) throws ElementAlreadyExistException {
		String key = et.getKey();
		//System.err.println("inside createElement Service setting key to:  = " + key);
		
		et.setKey(key);
		
		if (!this.dataBase.existsByKey(key)) {
			this.dataBase.save(et);
		} else {
			throw new ElementAlreadyExistException();
		}
	}


	@Override
	@Transactional
	@MyLog
	public void updateElement(ElementEntity et) throws ElementNotFoundException {
		String key = et.getKey();
		if (this.dataBase.existsByKey(key)) {
			this.dataBase.deleteByKey(key); // delete not updated element
			this.dataBase.save(et); // save updated element
		} else {
			
			throw new ElementNotFoundException();
		}
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	public ElementEntity getElement(String playground, String id) throws ElementNotFoundException {
		String key = ElementEntity.createKeyFromIdAndPlayground(id, playground);
		if (this.dataBase.existsByKey(key)) {
			return this.dataBase.findByKey(key).get();
		} else {
			throw new ElementNotFoundException();
		}
	}

	@Override
	@Transactional
	@MyLog
	public void deleteElement(String playground, String id) {
		String key = ElementEntity.createKeyFromIdAndPlayground(id, playground);
		if (this.dataBase.existsByKey(key)) {
			this.dataBase.deleteByKey(key);
		}
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	public List<ElementEntity> getAllElements() {
		List<ElementEntity> list = this.dataBase.findAll(Sort.by("id")).stream().collect(Collectors.toList());
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	public List<ElementEntity> getAllElements(Pageable page)
			throws InvalidPageSizeRequestException, InvalidPageRequestException {

		if (page.getPageSize() < 1)
			throw new InvalidPageSizeRequestException();
		if (page.getPageNumber() < 0)
			throw new InvalidPageRequestException();

		List<ElementEntity> list = this.dataBase.findAll(page).getContent();

		return list;
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	public List<ElementEntity> getAllElementsNearBy(double x, double y, double distance, Pageable page)
			throws InvalidDistanceValueException {

		if (distance < 0) {
			throw new InvalidDistanceValueException(
					"when searching elements who is near by, distance must be bigger or equal to 0");
		} else {
			List<ElementEntity> list = this.dataBase.findAll(page).getContent().stream()
					.filter(ee -> isNear(ee, x, y, distance)).collect(Collectors.toList());
			return list;
		}

	}

	
	
	//TODO insteas get all elements and filter, do filter inside database
	@Override
	@Transactional(readOnly = true)
	@MyLog
	public List<ElementEntity> getAllElementsByAttributeAndValue(String attribute, String value,Pageable page)
			throws InvalidAttributeNameException {
		List<ElementEntity> filteredElements;

		switch (attribute) {

		case "name": {
			filteredElements = this.getAllElements().stream().filter(e -> e.getName().equals(value))
					.collect(Collectors.toList());
			break;
		}
		case "type": {
			filteredElements = this.getAllElements().stream().filter(e -> e.getType().equals(value))
					.collect(Collectors.toList());
			break;
		}

		default:
			throw new InvalidAttributeNameException("Attribute Name does not exist in Element");
		}

		return filteredElements.stream().skip(page.getPageSize() * page.getPageNumber()).limit(page.getPageSize()).collect(Collectors.toList());
	}

	@Override
	@Transactional
	@MyLog
	public void cleanup() {
		this.dataBase.deleteAll();
	}


	private boolean isNear(ElementEntity et, double x, double y, double distance) {
		double etX = et.getX();
		double etY = et.getY();

		if (etX < x - distance || etX > x + distance) { // check if x isn't in range
			return false;
		}
		if (etY < y - distance || etY > y + distance) { // check if y isn't in range
			return false;
		}

		return true;
	}
	
	
	public static void setIDToZero() {
		ID = 0;
	}

	
}
