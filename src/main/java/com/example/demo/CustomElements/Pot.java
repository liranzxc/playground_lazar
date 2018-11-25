package com.example.demo.CustomElements;

import java.util.Date;
import java.util.Map;

import com.example.demo.classes.Location;
import com.example.demo.classes.EntityClasses.ElementEntity;

public class Pot extends ElementEntity{

	public Pot() {
		super();
	}

	public Pot(String playground, String id, Location location, String name, Date creationDate, Date expireDate,
			String type, Map<String, Object> attributes, String creatorPlayground, String creatorEmail) {
		super(playground, id, location, name, creationDate, expireDate, type, attributes, creatorPlayground, creatorEmail);
	}

}
