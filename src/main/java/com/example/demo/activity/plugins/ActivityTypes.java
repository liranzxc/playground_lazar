package com.example.demo.activity.plugins;

public enum ActivityTypes {
	BOARDPOST("BoardPost"), 
	BOARDREAD("BoardRead"),
	COOKOMMELETTE("CookOmelette"),
	ENTER_TO_REFRIGERATOR("EnterToRefrigerator");

	private String name;
	
	private ActivityTypes(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
}
