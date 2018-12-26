package com.example.demo.element;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.aop.MyLog;
import com.example.demo.aop.PermisionLog;
import com.example.demo.application.exceptions.InvalidPageRequestException;
import com.example.demo.application.exceptions.InvalidPageSizeRequestException;
import com.example.demo.element.exceptions.ElementAlreadyExistException;
import com.example.demo.element.exceptions.ElementNotFoundException;
import com.example.demo.element.exceptions.InvalidAttributeNameException;
import com.example.demo.element.exceptions.InvalidDistanceValueException;
import com.example.demo.user.TypesEnumUser.Types;

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
	@PermisionLog(value={Types.Manager})
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
	@PermisionLog(value={})  // niegther player nor manager have permision
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
	@PermisionLog(value={Types.Manager})
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
	@PermisionLog(value={Types.Manager})
	public void deleteElement(String playground, String id) {
		
		String key = ElementEntity.createKeyFromIdAndPlayground(id, playground);
		if (this.dataBase.existsByKey(key)) {
			ElementEntity et = this.dataBase.findByKey(key).get();
			et.setExpireDate(new Date()); // empty constructor give now date
			
			this.dataBase.deleteByKey(key);		
			this.dataBase.save(et);
			
		}
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	public List<ElementEntity> getAllElements() {
		return this.dataBase.findAll(Sort.by("id"));
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	@PermisionLog(value={Types.Manager})
	public List<ElementEntity> getAllElementsManager(Pageable page)
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
	@PermisionLog(value={Types.Player})
	public List<ElementEntity> getAllElementsPlayer(Pageable page)
			throws InvalidPageSizeRequestException, InvalidPageRequestException {

		if (page.getPageSize() < 1)
			throw new InvalidPageSizeRequestException();
		if (page.getPageNumber() < 0)
			throw new InvalidPageRequestException();

		List<ElementEntity> list = this.dataBase.findByExpireDateGreaterThan(new Date(), page);

		return list;
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	@PermisionLog(value={Types.Manager})
	public List<ElementEntity> getAllElementsNearByManager(double x, double y, double distance, Pageable page)
			throws InvalidDistanceValueException {

		if (distance < 0) {
			throw new InvalidDistanceValueException(
					"when searching elements who is near by, distance must be bigger or equal to 0");
		} else {
			List<ElementEntity> list = this.dataBase.findAll().stream()
					.filter(ee -> isNear(ee, x, y, distance))
					.skip(page.getPageSize() * page.getPageNumber())
					.limit(page.getPageSize())
					.collect(Collectors.toList());
			return list;
		}

	}
	
	
	@Override
	@Transactional(readOnly = true)
	@MyLog
	@PermisionLog(value={Types.Player})
	public List<ElementEntity> getAllElementsNearByPlayer(double x, double y, double distance, Pageable page)
			throws InvalidDistanceValueException {

		if (distance < 0) {
			throw new InvalidDistanceValueException(
					"when searching elements who is near by, distance must be bigger or equal to 0");
		} else {
			List<ElementEntity> list = this.dataBase.findByExpireDateGreaterThan(new Date()).stream()
					.filter(ee -> isNear(ee, x, y, distance))
					.skip(page.getPageSize() * page.getPageNumber())
					.limit(page.getPageSize())
					.collect(Collectors.toList());
			return list;
		}
	}

	
	@Override
	@Transactional(readOnly = true)
	@MyLog
	@PermisionLog(value={Types.Manager})
	public List<ElementEntity> getAllElementsByAttributeAndValueManager(String attribute, String value,Pageable page)
			throws InvalidAttributeNameException {
		switch (attribute) {

		case "name": {
			return this.dataBase.findByName(value, page);
		}
		case "type": {
			return this.dataBase.findByType(value, page);
		}

		default:
			throw new InvalidAttributeNameException("Attribute Name does not exist in Element");
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	@MyLog
	@PermisionLog(value={Types.Player})
	public List<ElementEntity> getAllElementsByAttributeAndValuePlayer(String attribute, String value,Pageable page)
			throws InvalidAttributeNameException {
		switch (attribute) {

		case "name": {
			return this.dataBase.findByNameAndExpireDateGreaterThan(value, new Date() ,page);
		}
		case "type": {
			return this.dataBase.findByTypeAndExpireDateGreaterThan(value, new Date() ,page);		}

		default:
			throw new InvalidAttributeNameException("Attribute Name does not exist in Element");
		}
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
