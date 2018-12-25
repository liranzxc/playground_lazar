package com.example.demo.activity.plugins;

import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.example.demo.activity.ActivityEntity;
import com.example.demo.activity.ActivityRepository;
import com.example.demo.activity.plugins.accessories.BoardMessagePageable;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BoardReadPlugin implements PlaygroundPlugin{
	private ActivityRepository repository;
	private ObjectMapper jackson;

	@PostConstruct
	public void init() {
		this.jackson = new ObjectMapper();
	}

	@Override
	public Object invokeOperation(ActivityEntity et) {
		try {
			BoardMessagePageable messages = this.jackson.readValue(et.getJsonAttributes(), BoardMessagePageable.class);
			return null;
			
		}catch(Exception e) {
			throw new RuntimeException();
		}
	}
}
