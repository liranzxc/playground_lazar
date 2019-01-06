package com.example.demo.element;

import org.springframework.stereotype.Component;

import com.example.demo.aop.ElementEntityIdValue;
import com.example.demo.aop.ElementEntityPlaygroundValue;
import com.example.demo.aop.ElementExistInDB;

@Component
public class ElementVeirfyier {
	
	public ElementVeirfyier() {
		super();
	}
	
	@ElementExistInDB
	public void verifyElement(@ElementEntityPlaygroundValue String elementPlayground,
							  @ElementEntityIdValue String elementID) {
		
	}
	

}
