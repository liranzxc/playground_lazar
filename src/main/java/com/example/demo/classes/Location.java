package com.example.demo.classes;

public class location {
	
	private double x ;
	private double y;
	
	
	public location(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}



	public location() {
		super();
	}
	
	
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	

}
