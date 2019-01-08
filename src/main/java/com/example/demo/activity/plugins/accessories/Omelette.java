package com.example.demo.activity.plugins.accessories;

import java.util.Random;

import com.example.demo.activity.exceptions.InvalidEggSizeException;

public class Omelette {
	public enum EggSize{
		Small, Medium, Large, ExtraLarge;
	}
	
	public enum Quality{
		Poor, Average, Good, Excellent;
	}
	
	private String quality;
	private double size;
	private Long points;
	
	public Omelette() {
		
	}

	public Omelette(OmeletteRecipe recipe) {
		Random random = new Random();
		double qualityBias = getQualityBias(recipe.getEggSize());
		this.size = (random.nextDouble()*10*qualityBias)+10;
		this.quality = bake(qualityBias, random);
		this.points = Score(this.size, this.quality);
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
		else throw new InvalidEggSizeException("The egg size: " + eggSize +" does not exist.");
		
	}
	
	public Long Score(double size, String quality) {
		double qualityBias=1;
		if (quality.equals(Quality.Poor.toString()))
			qualityBias = 0.8;
		else if (quality.equals(Quality.Average.toString()))
			qualityBias = 1;
		else if (quality.equals(Quality.Good.toString()))
			qualityBias = 1.2;
		else if (quality.equals(Quality.Excellent.toString()))
			qualityBias = 1.5;
		
		return (long)(qualityBias * size);
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
	
	
	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}
	
}
