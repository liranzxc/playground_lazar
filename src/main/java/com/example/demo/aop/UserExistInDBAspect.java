package com.example.demo.aop;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.SoftException;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.user.UserService;
import com.example.demo.user.exceptions.InvalidEmailException;
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
	public void verifyUser(JoinPoint jp) throws InvalidEmailException, UserNotFoundException {
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
		
		//Find annotated argument
	    for (int i = 0; i < args.length; i++) {
	        for (Annotation annotation : annotations[i]) {
	            if (annotation.annotationType() == EmailValue.class) {
	                Object email = args[i];
	                System.err.println("annotation email is: " + email.toString());
	                if (email instanceof String) {
	                	if(isAnEmail((String)email)) {
	                		this.userService.getUser((String)email, "playground_lazar");
	                		return;
	                	}
	                	else {
	                		throw new InvalidEmailException("invalid email has been given");
	                	}
	                }
	            }
	        }
	    }
	}
	
	private boolean isAnEmail(String email) {
		return Pattern.matches("[_a-zA-Z1-9]+(\\.[A-Za-z0-9]*)*@[A-Za-z0-9]+\\.[A-Za-z0-9]+(\\.[A-Za-z0-9]*)*", email);
	}
}
