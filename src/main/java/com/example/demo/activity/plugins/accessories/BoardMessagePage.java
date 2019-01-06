package com.example.demo.activity.plugins.accessories;

import java.util.List;

public class BoardMessagePage {
	List<String> messages;

	public BoardMessagePage(List<String> messages) {
		super();
		this.messages = messages;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
}
