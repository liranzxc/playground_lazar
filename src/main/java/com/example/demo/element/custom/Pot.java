package com.example.demo.element.custom;

import java.util.Date;
import java.util.Map;

import com.example.demo.element.ElementEntity;

public class Pot extends ElementEntity{

	public Pot() {
		super();
	}

	public Pot(String playground, String id, double x, double y, String name, Date creationDate, Date expireDate,
			String type, Map<String, Object> attributes, String creatorPlayground, String creatorEmail) {
		super(playground, id, x, y, name, creationDate, expireDate, type, attributes, creatorPlayground, creatorEmail);
	}

}
