package com.example.demo.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.management.relation.InvalidRoleInfoException;

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

		
		List<String> allPermissions = getUsersTypesWithPermission(signature);
		System.err.println("&&& -- " + allPermissions.toString());
		
		

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
	            	System.err.println("found EmailValue annotation inside userPermission it is: " + args[i].toString());
	                Object email = args[i];
	                if (email instanceof String) {
	                	System.err.println("");
	                	if(isAnEmail((String)email)) {
	                		UserEntity userAccount = this.userService.getUser((String)email, "playground_lazar");
	                		
	                		String userRole = userAccount.getRole();
	                		System.err.println("userRole = " + userRole);
	                		
	                		for (String p : allPermissions) {
	                			System.err.println("p is = " + p);
								if(p.equals(userRole)) {
									System.err.println("out from aspect");
									return pjp.proceed(args);
								}
							}
	                		
	                		throw new InvalidRoleInfoException("The roles with permission are: " + allPermissions.toString()
	                		+ ",\n but your user' role is: " + userRole);
	                		//args[i] = userRole;
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


	private List<String> getUsersTypesWithPermission(MethodSignature signature) {
		Method method = signature.getMethod();
		UserPermission permissionAnnotation = method.getAnnotation(UserPermission.class);
		Types[] permissionsTypes = permissionAnnotation.permissions();
		List<String> permissions = new ArrayList<>();
		for (Types type : permissionsTypes) {
			permissions.add(type.getType());
		}
		return permissions;
	}
	
	// TODO: understand what happend here
//	private boolean isAnEmail(String email) {
//		return Pattern.matches("[_a-zA-Z1-9]+(\\.[A-Za-z0-9]*)*@[A-Za-z0-9]+\\.[A-Za-z0-9]+(\\.[A-Za-z0-9]*)*", email);
//	}
	
	private boolean isAnEmail(String email) {
		String[] withoutShtrudel = email.split("@");
		return (withoutShtrudel.length == 2);
	}
	
}
