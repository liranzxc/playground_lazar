package com.example.demo.activity.plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.activity.ActivityEntity;
import com.example.demo.activity.plugins.accessories.BoardMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BoardPostPlugin implements PlaygroundPlugin {
	private ObjectMapper jackson;
	

	
	@Autowired
	public BoardPostPlugin() {
		this.jackson = new ObjectMapper();
	}
	
	
	@Override
	public Object invokeOperation(ActivityEntity et) {
		try {
			BoardMessage message = this.jackson.readValue(et.getJsonAttributes(), BoardMessage.class);
			return message;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
