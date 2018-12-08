package com.example.demo.customelements;

import java.util.Date;
import java.util.Map;

import com.example.demo.classes.Location;
import com.example.demo.classes.entities.ElementEntity;

public class Pot extends ElementEntity{

	public Pot() {
		super();
	}

	public Pot(String playground, String id, Location location, String name, Date creationDate, Date expireDate,
			String type, Map<String, Object> attributes, String creatorPlayground, String creatorEmail) {
		super(playground, location, name, creationDate, expireDate, type, attributes, creatorPlayground, creatorEmail);
	}

}
