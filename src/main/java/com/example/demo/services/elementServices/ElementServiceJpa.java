package com.example.demo.services.elementServices;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.IElementRepository;
import com.example.demo.classes.Location;
import com.example.demo.classes.EntityClasses.ElementEntity;
import com.example.demo.classes.exceptions.ElementAlreadyExistException;
import com.example.demo.classes.exceptions.ElementNotFoundException;
import com.example.demo.classes.exceptions.InvalidAttributeNameException;
import com.example.demo.classes.exceptions.InvalidDistanceValueException;

@Service
public class ElementServiceJpa implements IElementService {

	@Autowired
	private IElementRepository dataBase;
	
	//TODO fix this class, basic logic has been laid out, but 
	// things like transactions and key logic need to be apply
	
	@Override
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
	public void deleteElement(String playground, String id) throws ElementNotFoundException {
		String key = generateKeyFromPlaygroundAndId(playground, id);
		if(this.dataBase.existsById(key)) {
			this.dataBase.deleteById(key);
		}
		else {
			throw new ElementNotFoundException();
		}
		
	}

	@Override
	public List<ElementEntity> getAllElements() {
		List<ElementEntity> list = StreamSupport.
				stream(this.dataBase.findAll().spliterator(), false)
				.collect(Collectors.toList());
		
		return list;
	}

	@Override
	public List<ElementEntity> getAllElements(int size, int page) {
		List<ElementEntity> list = StreamSupport
				.stream(this.dataBase.findAll().spliterator(), false)
				.skip(size *page)
				.limit(size)
				.collect(Collectors.toList());
		return list;
	}

	@Override
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
	public List<ElementEntity> getAllElementsByAttributeAndValue(String attribute, String value)
			throws InvalidAttributeNameException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cleanup() {
		this.dataBase.deleteAll();
	}
	
	private String getKeyFromElementEntity(ElementEntity et) {
		return et.getPlayground() + et.getId();
	}
	
	private String generateKeyFromPlaygroundAndId(String playground, String id) {
		String key = playground + id;
		return key;
	}
	
	private boolean isNear(ElementEntity et, double x, double y, double distance) {
		Location location = et.getLocation();
		double etX = location.getX();
		double etY = location.getY();
		
		if(etX < x - distance || etX > x + distance) { // check if x isn't in range
			return false;
		}
		if(etY < y - distance || etY > y + distance) { // check if y isn't in range
			return false;
		}
		
		return true;
	}


}
