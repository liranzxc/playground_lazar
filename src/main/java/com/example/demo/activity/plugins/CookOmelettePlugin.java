package com.example.demo.activity.plugins;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.example.demo.activity.ActivityEntity;
import com.example.demo.activity.plugins.accessories.Omelette;
import com.example.demo.activity.plugins.accessories.OmeletteRecipe;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.net.SyslogOutputStream;

@Component
public class CookOmelettePlugin implements PlaygroundPlugin{
	private ObjectMapper jackson;
	
	@PostConstruct
	public void init() {
		this.jackson = new ObjectMapper();
	}
	
	@Override
	public Object invokeOperation(ActivityEntity et) {
		try {
			OmeletteRecipe recipe = this.jackson.readValue(et.getJsonAttributes(), OmeletteRecipe.class);
			return new Omelette(recipe);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
