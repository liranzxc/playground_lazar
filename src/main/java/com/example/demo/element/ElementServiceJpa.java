package com.example.demo.element;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.aop.EmailValue;
import com.example.demo.aop.ToLog;
import com.example.demo.aop.UserPermission;
import com.example.demo.application.exceptions.InvalidPageRequestException;
import com.example.demo.application.exceptions.InvalidPageSizeRequestException;
import com.example.demo.element.exceptions.ElementAlreadyExistException;
import com.example.demo.element.exceptions.ElementNotFoundException;
import com.example.demo.element.exceptions.InvalidAttributeNameException;
import com.example.demo.element.exceptions.InvalidDistanceValueException;
import com.example.demo.user.exceptions.InvalidRoleException;

@Service
public class ElementServiceJpa implements ElementService {

	private static int ID = 0;
	//private ArrayList<String> roleTypes;
	
	private ElementRepository dataBase;

	@Autowired
	public void setDataBase(ElementRepository dataBase) {
		this.dataBase = dataBase;
	}
	
	
	@Override
	@Transactional
	@UserPermission
	@ToLog
	public void addNewElement(ElementEntity et, @EmailValue String email) throws ElementAlreadyExistException, InvalidRoleException {
		String role = email;
		if(!role.equals("Manager")) {
			throw new InvalidRoleException("only manager can add new Elements");
		}
		
		int newID = ++ID;
		String key = ElementEntity.createKeyFromIdAndPlayground(newID + "", et.getPlayground());
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
	@UserPermission
	@ToLog
	public void addElementFromOutside(ElementEntity et, @EmailValue String email) throws ElementAlreadyExistException, InvalidRoleException {
		String role = email;
		if(!role.equals("Manager")) {
			throw new InvalidRoleException("only manager can add new Elements");
		}
		
		String key = et.getKey();

		if (!this.dataBase.existsByKey(key)) {
			this.dataBase.save(et);
		} else {
			throw new ElementAlreadyExistException();
		}
	}

	@Override
	@Transactional
	@UserPermission
	@ToLog
	public void updateElement(ElementEntity et, @EmailValue String email) throws ElementNotFoundException, InvalidRoleException {
		String role = email;
		if(!role.equals("Manager")) {
			throw new InvalidRoleException("only manager can add new Elements");
		}
		
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
	@ToLog
	public List<ElementEntity> getAllElements() {
		return this.dataBase.findAll(Sort.by("id"));
	}

	@Transactional
	@UserPermission
	@ToLog
	@Override
	public ElementEntity getElement(String playground, String id, @EmailValue String email)
			throws ElementNotFoundException, InvalidRoleException {
		String role = email; // if gotten to this line email was swapped with type
		
		switch (role) {
		case ("Player"): {
			return getElementPlayer(playground, id);
		}
		case ("Manager"): {
			return getElementManager(playground, id);
		}
		}
		
		throw new InvalidRoleException("couldn't identify the role");
	}

	@Transactional
	@UserPermission
	@ToLog
	@Override
	public void deleteElement(String playground, String id, @EmailValue String email) throws InvalidRoleException {
		String role = email; // if gotten to this line email was swapped with type
		if (!role.equals("Manager")) {
			throw new InvalidRoleException("Only manager can delete elements");
		}

		String key = ElementEntity.createKeyFromIdAndPlayground(id, playground);
		if (this.dataBase.existsByKey(key)) {
			ElementEntity et = this.dataBase.findByKey(key).get();
			et.setExpireDate(new Date()); // empty constructor give now date

			this.dataBase.deleteByKey(key);
			this.dataBase.save(et);
		}
	}

	@Transactional
	@UserPermission
	@ToLog
	@Override
	public List<ElementEntity> getAllElements(Pageable page,@EmailValue String email)
			throws InvalidPageSizeRequestException, InvalidPageRequestException, InvalidRoleException {
		String role = email; // if gotten to this line email was swapped with type
		System.err.println("getAllElements Role is: " + role);
		switch (role) {
		case ("Player"): {
			System.err.println("going to getAllElementPlayer");
			return getAllElementsPlayer(page);
		}
		case ("Manager"): {
			System.err.println("going to getAllElementManager");
			return getAllElementsManager(page);
		}
		}
		throw new InvalidRoleException("couldn't identify the role");
	}

	@Transactional
	@UserPermission
	@ToLog
	@Override
	public List<ElementEntity> getAllElementsNearBy(double x, double y, double distance,@EmailValue String email, Pageable page)
			throws InvalidDistanceValueException, InvalidRoleException {

		String role = email; // if gotten to this line email was swapped with type

		switch (role) {
		case ("Player"): {
			return getAllElementsNearByPlayer(x, y, distance, page);
		}
		case ("Manager"): {
			return getAllElementsNearByManager(x, y, distance, page);
		}
		}
		throw new InvalidRoleException("couldn't identify the role");
	}

	@Transactional
	@UserPermission
	@ToLog
	@Override
	public List<ElementEntity> getAllElementsByAttributeAndValue(String attribute, String value,@EmailValue String email,
			Pageable page) throws InvalidAttributeNameException, InvalidPageSizeRequestException,
			InvalidPageRequestException, InvalidRoleException {
		String role = email; // if gotten to this line email was swapped with type

		switch (role) {
		case ("Player"): {
			return getAllElementsByAttributeAndValuePlayer(attribute, value, page);
		}
		case ("Manager"): {
			return getAllElementsByAttributeAndValueManager(attribute, value, page);
		}
		}
		throw new InvalidRoleException("couldn't identify the role");
	}

	@Override
	@Transactional
	@ToLog
	public void cleanup() {
		this.dataBase.deleteAll();
	}

	private ElementEntity getElementPlayer(String playground, String id) throws ElementNotFoundException {
		String key = ElementEntity.createKeyFromIdAndPlayground(id, playground);
		if (this.dataBase.existsByKey(key)) {
			return this.dataBase.findAllByKeyAndExpireDateGreaterThanOrExpireDateIsNull(key, new Date()).get();
		} else {
			throw new ElementNotFoundException();
		}
	}

	private ElementEntity getElementManager(String playground, String id) throws ElementNotFoundException {
		String key = ElementEntity.createKeyFromIdAndPlayground(id, playground);
		if (this.dataBase.existsByKey(key)) {
			return this.dataBase.findByKey(key).get();
		} else {
			throw new ElementNotFoundException();
		}
	}

	private List<ElementEntity> getAllElementsManager(Pageable page)
			throws InvalidPageSizeRequestException, InvalidPageRequestException {

		verifyPageable(page);
		return this.dataBase.findAll(page).getContent();
	}

	private List<ElementEntity> getAllElementsPlayer(Pageable page)
			throws InvalidPageSizeRequestException, InvalidPageRequestException {

		verifyPageable(page);
		return this.dataBase.findAllByExpireDateGreaterThanOrExpireDateIsNull(new Date(), page);
	}

	private List<ElementEntity> getAllElementsNearByManager(double x, double y, double distance, Pageable page)
			throws InvalidDistanceValueException {

		if (distance < 0) {
			throw new InvalidDistanceValueException(
					"when searching elements who is near by, distance must be bigger or equal to 0");
		} else {
			List<ElementEntity> list = this.dataBase.findAll().stream().filter(ee -> isNear(ee, x, y, distance))
					.skip(page.getPageSize() * page.getPageNumber()).limit(page.getPageSize())
					.collect(Collectors.toList());
			return list;
		}

	}

	private List<ElementEntity> getAllElementsNearByPlayer(double x, double y, double distance, Pageable page)
			throws InvalidDistanceValueException {

		if (distance < 0) {
			throw new InvalidDistanceValueException(
					"when searching elements who is near by, distance must be bigger or equal to 0");
		} else {
			List<ElementEntity> list = this.dataBase.findAllByExpireDateGreaterThanOrExpireDateIsNull(new Date())
					.stream().filter(ee -> isNear(ee, x, y, distance)).skip(page.getPageSize() * page.getPageNumber())
					.limit(page.getPageSize()).collect(Collectors.toList());
			return list;
		}
	}

	private List<ElementEntity> getAllElementsByAttributeAndValueManager(String attribute, String value, Pageable page)
			throws InvalidAttributeNameException, InvalidPageSizeRequestException, InvalidPageRequestException {
		verifyPageable(page);

		switch (attribute) {

		case "name": {
			return this.dataBase.findAllByName(value, page);
		}
		case "type": {
			return this.dataBase.findAllByType(value, page);
		}

		default:
			throw new InvalidAttributeNameException("Attribute Name does not exist in Element");
		}
	}

	private List<ElementEntity> getAllElementsByAttributeAndValuePlayer(String attribute, String value, Pageable page)
			throws InvalidAttributeNameException, InvalidPageSizeRequestException, InvalidPageRequestException {

		verifyPageable(page);

		switch (attribute) {

		case "name": {
			return this.dataBase.findAllByNameAndExpireDateGreaterThanOrExpireDateIsNull(value, new Date(), page);
		}
		case "type": {
			return this.dataBase.findAllByTypeAndExpireDateGreaterThanOrExpireDateIsNull(value, new Date(), page);
		}

		default:
			throw new InvalidAttributeNameException("Attribute Name does not exist in Element");
		}
	}

	private void verifyPageable(Pageable page) throws InvalidPageSizeRequestException, InvalidPageRequestException {
		if (page.getPageSize() < 1)
			throw new InvalidPageSizeRequestException();
		if (page.getPageNumber() < 0)
			throw new InvalidPageRequestException();
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
