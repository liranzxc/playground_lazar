package com.example.demo.aop;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.regex.Pattern;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.SoftException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMapMethodArgumentResolver;

import com.example.demo.user.UserService;
import com.example.demo.user.exceptions.UserNotFoundException;

@Aspect
@Component
public class UserExistInDBAspect {

	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	@Before("@annotation(com.example.demo.aop.UserExistInDB)")
	public void verify(JoinPoint jp) throws UserNotFoundException {
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
		
		boolean userFound = false;
		
	    //Find annotated argument
	    for (int i = 0; i < args.length; i++) {
	        for (Annotation annotation : annotations[i]) {
	            if (annotation.annotationType() == PathVariable.class) {
	                Object arg = args[i];
	                if (arg instanceof String) {
	                	if(isAnEmail((String)arg)) {
	                		try {
	   	                	   this.userService.getUser((String)arg, "playground_lazar");
	   	                	   userFound = true;
	                		}
	   	                    catch(Exception e) {
	   	                	   //do nothing
	   	                    }
	                	}  
	                }
	            }
	        }
	    }
	    
	    if(!userFound) {
	    	throw new UserNotFoundException("no such email in the system");
	    }
	}
	
	
	private boolean isAnEmail(String email) {
		return Pattern.matches("[_a-zA-Z1-9]+(\\.[A-Za-z0-9]*)*@[A-Za-z0-9]+\\.[A-Za-z0-9]+(\\.[A-Za-z0-9]*)*", email);
	}
	
	
//	 Object[] args = pjp.getArgs();
//
//	    //get all annotations for arguments
//	    MethodSignature signature = (MethodSignature) pjp.getSignature();
//	    String methodName = signature.getMethod().getName();
//	    Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
//	    Annotation[][] annotations;
//	    try {
//	        annotations = pjp.getTarget().getClass().
//	                getMethod(methodName, parameterTypes).getParameterAnnotations();
//	    } catch (Exception e) {
//	        throw new SoftException(e);
//	    }
//
//	    //Find annotated argument
//	    for (int i = 0; i < args.length; i++) {
//	        for (Annotation annotation : annotations[i]) {
//	            if (annotation.annotationType() == ReplaceFooBar.class) {
//	                Object raw = args[i];
//	                if (raw instanceof String) {
//	                    // and replace it with a new value
//	                    args[i] = ((String) raw).replaceAll("foo", "bar");
//	                }
//	            }
//	        }
//	    }
//	    //execute original method with new args
//	    return pjp.proceed(args);
	 
	
}
