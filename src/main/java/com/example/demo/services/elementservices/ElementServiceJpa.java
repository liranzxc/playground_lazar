package com.example.demo.services.elementservices;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

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

	@Autowired
	private IElementRepository dataBase;

	@Override
	@Transactional
	public void addNewElement(ElementEntity et) throws ElementAlreadyExistException {
		String key = getKeyFromElementEntity(et);
		if (!this.dataBase.existsByKey(key)) {
			this.dataBase.save(et);
		} else {
			throw new ElementAlreadyExistException();
		}
	}

	@Override
	@Transactional
	public void updateElement(ElementEntity et) throws ElementNotFoundException {
		String key = getKeyFromElementEntity(et);
		if (this.dataBase.existsByKey(key)) {
			this.dataBase.save(et);
		} else {
			throw new ElementNotFoundException();
		}

	}

	@Override
	@Transactional(readOnly = true)
	public ElementEntity getElement(String playground, String id) throws ElementNotFoundException {
		String key = generateKeyFromPlaygroundAndId(playground, id);
		if (this.dataBase.existsByKey(key)) {
			return this.dataBase.findByKey(key).get();
		} else {
			throw new ElementNotFoundException();
		}
	}

	@Override
	@Transactional
	public void deleteElement(String playground, String id) {
		String key = generateKeyFromPlaygroundAndId(playground, id);
		if (this.dataBase.existsByKey(key)) {
			this.dataBase.deleteByKey(key);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> getAllElements() {
		List<ElementEntity> list = this.dataBase.findAll(Sort.by("id")).stream().collect(Collectors.toList());

		return list;
	}

	@Override
	@Transactional(readOnly = true)
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

	@Override
	@Transactional(readOnly = true)
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
	public void cleanup() {
		this.dataBase.deleteAll();
	}

	// TODO fix this method base on key that would be decided
	private String getKeyFromElementEntity(ElementEntity entity) {
		String key = entity.getKey();
		return key;
	}

	private String generateKeyFromPlaygroundAndId(String playground, String id) {
		String key = playground + "@@" + id;
		return key;
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

}
