package com.example.demo.element.custom;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.activity.plugins.ActivityTypes;
import com.example.demo.element.ElementEntity;

public class Board extends ElementEntity {

	public Board() {
		super();
	}
	
	public Board(ElementEntity et) {
		this(et.getPlayground(), et.getId(), et.getX(), et.getY(), et.getName(), et.getCreationDate(),
			  et.getExpireDate(), et.getType(), et.getAttributes(), et.getCreatorPlayground(), et.getCreatorEmail());
	}
	
	public Board(String playground, String id, double x, double y, String name, Date creationDate, Date expireDate, String type,
			Map<String, Object> attributes, String creatorPlayground, String creatorEmail) {
		super(playground, id, x, y, name, creationDate, expireDate, type, attributes, creatorPlayground, creatorEmail);
		
		this.attributes = new HashMap<String, Object>();
		this.attributes.put(ActivityTypes.BOARDPOST.getName(), new Long(10));
		this.attributes.put(ActivityTypes.BOARDREAD.getName(), new Long(1));
	}
}
