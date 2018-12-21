package com.example.demo.element.custom;

import java.util.Date;
import java.util.Map;

import com.example.demo.element.ElementEntity;
import com.example.demo.element.Location;

public class Pot extends ElementEntity{

	public Pot() {
		super();
	}

	public Pot(String playground, String id, Location location, String name, Date creationDate, Date expireDate,
			String type, Map<String, Object> attributes, String creatorPlayground, String creatorEmail) {
		super(playground, location, name, creationDate, expireDate, type, attributes, creatorPlayground, creatorEmail);
	}

}
