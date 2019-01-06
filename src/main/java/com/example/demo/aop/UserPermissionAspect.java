package com.example.demo.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.SoftException;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.user.TypesEnumUser.Types;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.InvalidRoleException;
import com.example.demo.user.exceptions.UserNotFoundException;

@Aspect
@Component
public class UserPermissionAspect {

	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	@Around("@annotation(com.example.demo.aop.UserPermission)")
	public Object getType(ProceedingJoinPoint  pjp) throws Throwable {
		
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
		
//		///////////////////////////////////////////////////
//		String[] permissions;
//		//Find annotated argument
//	    for (int i = 0; i < pjp.getSignature().; i++) {
//	        for (Annotation annotation : annotations[i]) {
//	        	//////////////////
//	        	if(annotation.annotationType() == UserPermission.class) {
//	        		System.err.println("found user permission annotation inside userPermission");
//	        		permissions = 
//	        	}
//	        }
//	    }
		
		 ////////////////////
		 Method method = signature.getMethod();
		 UserPermission permissionAnnotation = method.getAnnotation(UserPermission.class);
		 Types[] permissionsTypes = permissionAnnotation.permissions();
		 List<String> permissions = new ArrayList<>();
		 for (Types type : permissionsTypes) {
			 permissions.add(type.name());
		}
		 System.err.println("The only users that can use are: " + permissions.toString());
		
		 
		 
		 
		//Find annotated argument
	    for (int i = 0; i < args.length; i++) {
	        for (Annotation annotation : annotations[i]) {
	            if (annotation.annotationType() == EmailValue.class) {
	            	System.err.println("found EmailValue annotation inside userPermission it is: " + args[i].toString());
	                Object email = args[i];
	                if (email instanceof String) {
	                	System.err.println("");
	                	if(isAnEmail((String)email)) {
	                		UserEntity et = this.userService.getUser((String)email, "playground_lazar");
	                		String role = et.getRole();
	                		
	                		////////////////////
	                		if(!permissions.contains(role)) {
	                			System.err.println("The permission is not fit: the role is " + role + ", but the Permissions are for: " + permissions.toString());
//	                			throw new InvalidRoleException("only " + role + " can do the function: " + signature.toString());
	                			throw new InvalidRoleException("only " + permissions.toString() + " can do the function: " + signature.getName().toString());
	                		}
	                		
	                		args[i] = role;
							return pjp.proceed(args);
					
	                	}
	                	else {
	                		throw new InvalidEmailException("invalid email has been given");
	                	}
	                }
	            }
	        }
	    }
	    System.err.println("UserPermission Aspect getType: going to continue with regular args");
	    return pjp.proceed(args);
	}
	
	
	private boolean isAnEmail(String email) {
		return Pattern.matches("[_a-zA-Z1-9]+(\\.[A-Za-z0-9]*)*@[A-Za-z0-9]+\\.[A-Za-z0-9]+(\\.[A-Za-z0-9]*)*", email);
	}
	
}
