package com.example.demo.activity.plugins;

import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.example.demo.activity.ActivityEntity;
import com.example.demo.activity.ActivityEnumTypes.Activities;
import com.example.demo.activity.ActivityRepository;
import com.example.demo.activity.plugins.accessories.BoardMessage;
import com.example.demo.activity.plugins.accessories.BoardMessagePage;
import com.example.demo.activity.plugins.accessories.BoardMessagePageable;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BoardReadPlugin implements PlaygroundPlugin{
	private ActivityRepository repository;
	private ObjectMapper jackson;
	
	@Autowired
	public BoardReadPlugin(ActivityRepository repository) {
		this.jackson = new ObjectMapper();
		this.repository = repository;
	}

	@Override
	public Object invokeOperation(ActivityEntity et) {
		try {
			BoardMessagePageable messagesPageable = this.jackson.readValue(et.getJsonAttributes(), BoardMessagePageable.class);
			BoardMessagePage messagePage =  new BoardMessagePage(this.repository.findByType(
					Activities.BoardPost.getName(),
					PageRequest.of(messagesPageable.getPage(), messagesPageable.getSize(), Direction.ASC, "key"))
					.stream()
					.map(BoardReadPlugin::getMessageFromBoardMessage)
					.collect(Collectors.toList()));
			return messagePage;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public static String getMessageFromBoardMessage(ActivityEntity et) {
		return et.getAttributes().get("poster").toString() +": " +et.getAttributes().get("message").toString();
	}
}
