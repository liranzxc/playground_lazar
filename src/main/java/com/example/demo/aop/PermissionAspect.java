package com.example.demo.aop;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.SoftException;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.UserNotFoundException;

@Aspect
@Component
public class PermissionAspect {

	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	@Around("@annotation(com.example.demo.aop.PermissionLog)")
	public Object getType(ProceedingJoinPoint  pjp) throws InvalidEmailException, UserNotFoundException {
		Object[] args = pjp.getArgs();	
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		String methodName = signature.getMethod().getName();
		Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
		
		Annotation[][] annotations;
		try {
			annotations = pjp.getTarget().getClass().getMethod(methodName, parameterTypes).getParameterAnnotations();
		} catch (Exception e) {
			throw new SoftException(e);
		}
		
		//Find annotated argument
	    for (int i = 0; i < args.length; i++) {
	        for (Annotation annotation : annotations[i]) {
	            if (annotation.annotationType() == EmailValue.class) {
	                Object email = args[i];
	                if (email instanceof String) {
	                	if(isAnEmail((String)email)) {
	                		UserEntity et = this.userService.getUser((String)email, "playground_lazar");
	                		String role = et.getRole();
	                		args[i] = role;
	                		try {
								return pjp.proceed(args);
							} catch (Throwable e) {
								System.err.println("inside PermissionAspect proceed failed:");
								System.err.println(e.getMessage());
							}  		
	                	}
	                	else {
	                		throw new InvalidEmailException("invalid email has been given");
	                	}
	                }
	            }
	        }
	    }
	    
	    throw new UserNotFoundException("no email has been found");
	}
	
	
	private boolean isAnEmail(String email) {
		return Pattern.matches("[_a-zA-Z1-9]+(\\.[A-Za-z0-9]*)*@[A-Za-z0-9]+\\.[A-Za-z0-9]+(\\.[A-Za-z0-9]*)*", email);
	}
	
}
