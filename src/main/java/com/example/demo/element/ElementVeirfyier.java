package com.example.demo.element;

import com.example.demo.aop.ElementEntityIdValue;
import com.example.demo.aop.ElementEntityPlaygroundValue;
import com.example.demo.aop.ElementExistInDB;

public class ElementVeirfyier {
	
	public ElementVeirfyier() {
		super();
	}
	
	@ElementExistInDB
	public void verifyElement(@ElementEntityPlaygroundValue String elementPlayground,
							  @ElementEntityIdValue String elementID) {
		
	}
	

}
