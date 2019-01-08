package com.example.demo.activity.plugins.accessories;

import java.util.Random;

public class Omelette {
	public enum EggSize{
		Small, Medium, Large, ExtraLarge;
	}
	
	public enum Quality{
		Poor, Average, Good, Excellent;
	}
	
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
				return Quality.Poor.toString();
			}
			else if (qual >=30 && qual < 60) {
				return Quality.Average.toString();
			}
			else if (qual >=60 && qual <100) {
				return Quality.Good.toString();
			}
			else return Quality.Excellent.toString();
	}

	public double getQualityBias(String eggSize) {
		if (eggSize.equals(EggSize.Small.toString())) {
			return 0.7;
		}
		else if (eggSize.equals(EggSize.Medium.toString())) {
			return 0.85;
		}
		else if (eggSize.equals(EggSize.Large.toString())) {
			return 1.1;
		}
		else if (eggSize.equals(EggSize.ExtraLarge.toString())) {
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
