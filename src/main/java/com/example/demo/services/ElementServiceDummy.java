package com.example.demo.services;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.example.demo.classes.Location;
import com.example.demo.classes.EntityClasses.ElementEntity;
import com.example.demo.classes.exceptions.ElementAlreadyExistException;
import com.example.demo.classes.exceptions.ElementNotFoundException;
import com.example.demo.classes.exceptions.InvalidAttributeNameException;
import com.example.demo.classes.exceptions.InvalidDistanceValueException;

@Service
public class ElementServiceDummy implements IElementService {
	
	private Map<String, ElementEntity> entities;
	
	@PostConstruct
	public void init() {
		this.entities = new ConcurrentHashMap<>();
		// maybe add details ?? 
		
	}

	@Override
	public void addNewElement(ElementEntity et) throws ElementAlreadyExistException {
		String newElementKey = getKeyFromElementEntity(et);
		if(this.entities.containsKey(newElementKey)) {
			throw new ElementAlreadyExistException("entity already exist in database");
		}
		else {
			this.entities.put(newElementKey, et);
		}
	}

	@Override
	public void updateElement(ElementEntity et) throws ElementNotFoundException {
		String updateElementKey = getKeyFromElementEntity(et);
		if(this.entities.containsKey(updateElementKey)) {
		
			//extract element from the dataBase and update the relevant fields
			ElementEntity dbElement = this.entities.get(updateElementKey);
			if(et.getLocation() != null) {
				dbElement.setLocation(et.getLocation());
			}
			if(et.getName() != null) {
				dbElement.setName(et.getName());
			}
			if(et.getCreatorPlayground() != null) {
				dbElement.setCreatorPlayground(et.getCreatorPlayground());
			}
			if(et.getCreatorEmail() != null) {
				dbElement.setCreatorEmail(et.getCreatorEmail());
			}
			
			//override existing entity with updated entity 
			this.entities.put(updateElementKey, dbElement);	
		}
		else {
			throw new ElementNotFoundException("element isn't in the DateBase");
		}
		
	}

	@Override
	public ElementEntity getElement(String playground, String id) throws ElementNotFoundException {
		String elementKey = playground + id;
		if(this.entities.containsKey(elementKey)) {
			return this.entities.get(elementKey);
		}
		else{
			throw new ElementNotFoundException();
		}
	}
	
	@Override
	public void deleteElement(String playground, String id) throws ElementNotFoundException {
		String elementKey = playground + id;
		if(this.entities.containsKey(elementKey)) {
			this.entities.remove(elementKey);
		}
		else{
			throw new ElementNotFoundException();
		}
		
	}

	@Override
	public List<ElementEntity> getAllElements() {
		return this.entities
				.values()
				.stream()
				.collect(Collectors.toList());
	}

	@Override
	public List<ElementEntity> getAllElementsNearBy(double x, double y, double distance) throws InvalidDistanceValueException {
		if(distance < 0)
			throw new InvalidDistanceValueException("Invalid distance value");
		
		return this.entities
				.values()
			   .stream() 
			   .filter(ee -> isNear(ee, x, y, distance))
			   .collect(Collectors.toList());
		
	}
	
	
	@Override
	public void cleanup() {
		this.entities.values().clear();
	}
	
	private String getKeyFromElementEntity(ElementEntity et) {
		return et.getPlayground() + et.getId();
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

	@Override
	public List<ElementEntity> getAllElements(int size, int page) {
		return this.entities
				.values()
				.stream()
				.skip(page * size)
				.limit(size)
				.collect(Collectors.toList());
	}

	@Override
	public List<ElementEntity> getAllElementsByAttributeAndValue(String attribute, String value) throws InvalidAttributeNameException {
		List<ElementEntity> filteredElements;
		
		switch (attribute) {
		// TODO: add cases that wasn't written
		case "playground":
			filteredElements = 
				this.entities
					.values()
					.stream()
					.filter(e -> e.getPlayground().equals(value))
					.collect(Collectors.toList());
			break;
			
		case "id":
			filteredElements = 
			this.entities
				.values()
				.stream()
				.filter(
						e -> e.getId().equals(value))
				.collect(Collectors.toList());
			break;
			
//		case "location":
//			// TODO: transfer value to location
//			String[] loactionCordinatesAsStrings = value.split(",");
//			double[] loactionCordinates = {Double.parseDouble(loactionCordinatesAsStrings[0]), 
//					Double.parseDouble(loactionCordinatesAsStrings[1])};
//			Location location = new Location(loactionCordinates[0], loactionCordinates[1]);
//			return (ElementTO[]) allElementsList
//					.stream()
//					.filter(
//							e -> e.getLocation().equals(location))  
//					.toArray();
		case "name":
			filteredElements = 
			this.entities
				.values()
				.stream()
				.filter(
						e -> e.getName().equals(value))
				.collect(Collectors.toList());
			break;
			
		case "type":
			filteredElements = 
			this.entities
				.values()
				.stream()
				.filter(
						e -> e.getType().equals(value))
				.collect(Collectors.toList());
			break;
			
		case "creatorPlayground":
			filteredElements = 
			this.entities
				.values()
				.stream()
				.filter(
						e -> e.getCreatorPlayground().equals(value))
				.collect(Collectors.toList());
			break;
			
		case "creatorEmail":
			filteredElements = 
			this.entities
				.values()
				.stream()
				.filter(
						e -> e.getCreatorEmail().equals(value))
				.collect(Collectors.toList());
			break;
			
		default:
			throw new InvalidAttributeNameException("Attribute Name does not exist in Element");
		}		
		
		return filteredElements;
//		return filteredElements.stream()
//						.skip(size*page)
//						.limit(size)
//						.collect(Collectors.toList());	
	}
}
