package com.example.demo.activity.plugins.accessories;

import java.util.List;

public class BoardMessagePage {
	List<BoardMessage> messages;

	public BoardMessagePage(List<BoardMessage> messages) {
		super();
		this.messages = messages;
	}

	public List<BoardMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<BoardMessage> messages) {
		this.messages = messages;
	}
}
