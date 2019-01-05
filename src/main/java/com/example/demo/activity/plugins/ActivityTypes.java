package com.example.demo.activity.plugins;

public enum ActivityTypes {
	BOARDPOST("BoardPost", new Long(10)), 
	BOARDREAD("BoardRead", new Long(1)),
	COOKOMMELETTE("CookOmelette", new Long(50)),
	ENTER_TO_REFRIGERATOR("EnterToRefrigerator", new Long(100));

	private String name;
	private Long points;
	
	private ActivityTypes(String name, Long points) {
		this.name = name;
		this.points = points;
	}
	
	public String getName() {
		return this.name;
	}

	public Long getPoints() {
		return this.points;
	}
	
}
