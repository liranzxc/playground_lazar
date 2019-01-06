package com.example.demo.activity.plugins;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.activity.ActivityEntity;
import com.example.demo.activity.ActivityRepository;
import com.example.demo.activity.plugins.accessories.BoardMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BoardPostPlugin implements PlaygroundPlugin {
	private ObjectMapper jackson;
	
//	@PostConstruct
//	public void init() {
//		this.jackson = new ObjectMapper();
//	}
	
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
