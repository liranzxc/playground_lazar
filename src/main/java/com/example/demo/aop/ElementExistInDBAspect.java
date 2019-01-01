package com.example.demo.aop;

import java.lang.annotation.Annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.SoftException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.element.ElementEntity;
import com.example.demo.element.ElementRepository;
import com.example.demo.element.ElementService;
import com.example.demo.element.exceptions.ElementNotFoundException;


@Aspect
@Component
public class ElementExistInDBAspect {
	
	private ElementRepository dataBase;
	
	@Autowired
	public void setDataBase(ElementRepository dataBase) {
		this.dataBase = dataBase;
	}
	
		
	@Before("@annotation(com.example.demo.aop.ElementExistInDB)")
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
	    	if(!this.dataBase.existsByKey(ElementEntity.createKeyFromIdAndPlayground(id, playground)) ){
	    		throw new ElementNotFoundException("no matching element for the given playground and id");
	    	}
	    }
	    else {
	    	throw new ElementNotFoundException("didnt found playground or id");
	    }
	}


}