package com.example.demo.element.custom;

public enum ElementTypes {
	BOARD("Board"),
	FRIDGE("Fridge"),
	POT("Pot");
	
	private String name;
	
	private ElementTypes(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
