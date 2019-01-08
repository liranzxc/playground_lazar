package com.example.demo.element.custom;

public enum ElementTypes {
	Board("Board"),
	Fridge("Fridge"),
	Pot("Pot");
	
	private String name;
	
	private ElementTypes(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
