package com.example.demo.activity.plugins.accessories;

import java.util.Random;

public class Omelette {
	private String quality;
	private double size;
	
	public Omelette() {
		
	}
	

	public Omelette(OmeletteRecipe recipe) {
		Random random = new Random();
		double qualityBias = getQualityBias(recipe.getEggSize());
		this.size = (random.nextDouble()*10*qualityBias)+10;
		this.quality = bake(qualityBias, random);
	}
	
	private String bake(double qualityBias, Random random) {
			double qual = random.nextDouble()*150*qualityBias;
			if (qual < 30) {
				return "Poor";
			}
			else if (qual >=30 && qual < 60) {
				return "Average";
			}
			else if (qual >=60 && qual <100) {
				return "Good";
			}
			else return "Excellent";
	}

	public double getQualityBias(String eggSize) {
		if (eggSize.equals("small")) {
			return 0.7;
		}
		else if (eggSize.equals("medium")) {
			return 0.85;
		}
		else if (eggSize.equals("large")) {
			return 1.1;
		}
		else if (eggSize.equals("extraLarge")) {
			return 1.4;
		}
		else throw new RuntimeException();
		
	}
	
	//for jackson

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}
	
}
