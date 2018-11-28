package com.example.demo.services.elementservices;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.classes.Location;
import com.example.demo.classes.entities.ElementEntity;
import com.example.demo.classes.exceptions.ElementAlreadyExistException;
import com.example.demo.classes.exceptions.ElementNotFoundException;
import com.example.demo.classes.exceptions.InvalidAttributeNameException;
import com.example.demo.classes.exceptions.InvalidDistanceValueException;
import com.example.demo.classes.exceptions.InvalidPageRequestException;
import com.example.demo.classes.exceptions.InvalidPageSizeRequestException;
import com.example.demo.repository.IElementRepository;

@Service
public class ElementServiceJpa implements IElementService {
	
	// TODO: Enum or final integers for Pagination parameters: default page and default size

	@Autowired
	private IElementRepository dataBase;
	
	//TODO fix this class, basic logic has been laid out, but 
	// things like transactions and key logic need to be apply
	
	@Override
	@Transactional
	public void addNewElement(ElementEntity et) throws ElementAlreadyExistException {
		String key = getKeyFromElementEntity(et);
		if(!this.dataBase.existsById(key)) {
			this.dataBase.save(et);
		}
		else {
			throw new ElementAlreadyExistException();
		}
	}

	@Override
	@Transactional
	public void updateElement(ElementEntity et) throws ElementNotFoundException {
		String key = getKeyFromElementEntity(et);
		if(this.dataBase.existsById(key)) {
			this.dataBase.save(et);
		}
		else {
			throw new ElementNotFoundException();
		}
		
	}

	@Override
	@Transactional(readOnly=true)
	public ElementEntity getElement(String playground, String id) throws ElementNotFoundException {
		String key = generateKeyFromPlaygroundAndId(playground, id);
		if(this.dataBase.existsById(key)) {
			return this.dataBase.findById(key).get();
		}
		else {
			throw new ElementNotFoundException();
		}
	}

	@Override
	@Transactional
	public void deleteElement(String playground, String id)  {
		String key = generateKeyFromPlaygroundAndId(playground, id);
		if(this.dataBase.existsById(key)) {
			this.dataBase.deleteById(key);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> getAllElements() {
		List<ElementEntity> list = StreamSupport.
				stream(this.dataBase.findAll().spliterator(), false)
				.collect(Collectors.toList());
		
		return list;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> getAllElements(int size, int page) throws InvalidPageSizeRequestException, InvalidPageRequestException {
		if(size < 1)
			throw new InvalidPageSizeRequestException();
		if(page < 0)
			throw new InvalidPageRequestException();
		
		List<ElementEntity> list = StreamSupport
				.stream(this.dataBase.findAll().spliterator(), false)
				.skip(size *page)
				.limit(size)
				.collect(Collectors.toList());
		

		return list;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> getAllElementsNearBy(double x, double y, double distance)
			throws InvalidDistanceValueException {
		
		if(distance < 0) {
			throw new InvalidDistanceValueException("when searching elements who is near by, distance must be bigger or equal to 0");
		}
		else {
			List<ElementEntity> list = StreamSupport
					.stream(this.dataBase.findAll().spliterator(), false)
					.filter(ee -> isNear(ee, x, y, distance))
					.collect(Collectors.toList());
			return list;
		}
		
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> getAllElementsByAttributeAndValue(String attribute, String value)
			throws InvalidAttributeNameException {
		List<ElementEntity> filteredElements;
		
		switch (attribute) {
		
		case "name":
		{
			filteredElements = this.getAllElements()
					.stream()
					.filter(e -> e.getName().equals(value))
					.collect(Collectors.toList());
			break;
		}
		case "type":
		{
			filteredElements = this.getAllElements()
					.stream()
					.filter(e -> e.getType().equals(value))
					.collect(Collectors.toList());
			break;
		}
			
		
			
		default:
			throw new InvalidAttributeNameException("Attribute Name does not exist in Element");
		}	
		return filteredElements;
	}

	@Override
	@Transactional
	public void cleanup() {
		this.dataBase.deleteAll();
	}
	
	//TODO fix this method base on key that would be decided
	private String getKeyFromElementEntity(ElementEntity entity) {
		String key =  entity.getId();
		return key;
	}
	
	private String generateKeyFromPlaygroundAndId(String playground, String id) {
		String key = id;
		return key;
	}
	
	private boolean isNear(ElementEntity et, double x, double y, double distance) {
		double etX = et.getX();
		double etY = et.getY();
		
		if(etX < x - distance || etX > x + distance) { // check if x isn't in range
			return false;
		}
		if(etY < y - distance || etY > y + distance) { // check if y isn't in range
			return false;
		}
		
		return true;
	}


}
