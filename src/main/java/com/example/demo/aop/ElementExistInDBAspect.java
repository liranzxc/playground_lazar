package com.example.demo.aop;

import java.lang.annotation.Annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.SoftException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.element.ElementService;
import com.example.demo.element.exceptions.ElementNotFoundException;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.UserNotFoundException;

@Aspect
@Component
public class ElementExistInDBAspect {
	
	private ElementService elementService;
	
	@Autowired
	public void setUserService(ElementService elementService) {
		this.elementService = elementService;
	}
	
		
	@Before("@annotation(com.example.demo.aop.UserExistInDB)")
	public void verifyElement(JoinPoint jp) throws ElementNotFoundException {
		Object[] args = jp.getArgs();	
		MethodSignature signature = (MethodSignature) jp.getSignature();
		String methodName = signature.getMethod().getName();
		Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
		
		Annotation[][] annotations;
		try {
			annotations = jp.getTarget().getClass().getMethod(methodName, parameterTypes).getParameterAnnotations();
		} catch (Exception e) {
			throw new SoftException(e);
		}
		
		String playground = null;
		String id = null;
		
		
		Object currentArg;
		//Find annotated argument
	    for (int i = 0; i < args.length; i++) {
	    	currentArg = args[i];
	        for (Annotation annotation : annotations[i]) {
	            if (annotation.annotationType() == ElementEntityPlaygroundValue.class) {
	                playground = (String)currentArg;
	            }
	            else if(annotation.annotationType() == ElementEntityIdValue.class) {
	            	id = (String)currentArg;
	            }
	        }
	    }
	    
	    
	    if(playground != null && id != null) {
	    	this.elementService.getElementPlayer(playground, id);
	    }
	    else {
	    	throw new ElementNotFoundException("didnt found playground or id");
	    }
	}


}
