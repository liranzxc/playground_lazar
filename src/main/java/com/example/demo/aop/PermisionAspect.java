package com.example.demo.aop;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.SoftException;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.user.UserService;
import com.example.demo.user.TypesEnumUser.Types;

@Component
@Aspect
public class PermisionAspect {

	
	private Log permisionLog = LogFactory.getLog(PermisionAspect.class);	
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	@Before("@annotation(com.example.demo.aop.PermisionLog)")
	public void log (JoinPoint joinPoint)  {
		
		System.err.println("*** entered To Annotaion' aspect!");
		
		Object[] args = joinPoint.getArgs();
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String methodName = signature.getMethod().getName();
		Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
		Annotation[][] annotations;
		try {
			annotations = joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes).getParameterAnnotations();
		} catch (Exception e) {
			throw new SoftException(e);
		}
		
		// find permissions
		Types[] permisions;
		for (int i = 0; i < args.length; i++) {
	        for (Annotation annotation : annotations[i]) {
	        	if(annotation.annotationType() == PathVariable.class) {  // Check if the annotation is "PermisionLog" Type
	        		permisions = ((PermisionLog) (annotation)).value();
	        	}
	        }
		}
		
		
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
            if (arg instanceof String && isAnEmail((String)arg)) {
//            	try {
//	                 this.userService.getUser((String)arg, "playground_lazar"); 
//	                 // TODO: get user type. if the type is contained in "permission" - there is no exception
//	                 // else: throw exception
//            	}
//	            catch(Exception e) {
//	                  //do nothing
//	            }  
            }
		}
	}
	
	private boolean isAnEmail(String email) {
		return Pattern.matches("[_a-zA-Z1-9]+(\\.[A-Za-z0-9]*)*@[A-Za-z0-9]+\\.[A-Za-z0-9]+(\\.[A-Za-z0-9]*)*", email);
	}
	
}
