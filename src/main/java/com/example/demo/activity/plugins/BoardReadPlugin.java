package com.example.demo.activity.plugins;

import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.example.demo.activity.ActivityEntity;
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
			BoardMessagePageable messagesPage = this.jackson.readValue(et.getJsonAttributes(), BoardMessagePageable.class);
			BoardMessagePage messagePage =  new BoardMessagePage(this.repository.findByType(
					"BoardMessage",
					PageRequest.of(messagesPage.getPage(), messagesPage.getSize(), Direction.ASC, "creationDate"))
					.stream()
					.map(BoardReadPlugin::getMessageFromBoardMessage)
					.collect(Collectors.toList()));
			System.err.println(messagePage.getMessages());
			return messagePage;
		}catch(Exception e) {
			throw new RuntimeException();
		}
	}
	
	public static String getMessageFromBoardMessage(ActivityEntity ac) {
		return ac.getAttributes().get("poster").toString() +": " +ac.getAttributes().get("message").toString();
	}
}
