package com.example.demo.element.custom;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.activity.ActivityTypes;
import com.example.demo.element.ElementEntity;

public class Board extends ElementEntity {

	public Board() {
		super();
	}

	public Board(ElementEntity entity) {
		super(entity);
		initBoardAttributes();
	}

	public Board(String playground, String id, double x, double y, String name, Date creationDate, Date expireDate,
			String type, Map<String, Object> attributes, String creatorPlayground, String creatorEmail) {
		super(playground, id, x, y, name, creationDate, expireDate, type, attributes, creatorPlayground, creatorEmail);
		initBoardAttributes();
	}

	private void initBoardAttributes() {
		this.attributes = new HashMap<String, Object>();
		String post = ActivityTypes.BoardPost.getActivityName();
		String read = ActivityTypes.BoardRead.getActivityName();
		this.attributes.put(post, 10L);
		this.attributes.put(read, 1L);
	}

}
