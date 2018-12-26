package com.example.demo.aop;

import java.lang.annotation.Annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.example.demo.user.TypesEnumUser.Types;

@Component
@Aspect
public class PermisionAspect {

	
	private Log permisionLog = LogFactory.getLog(PermisionAspect.class);
	
	
	@Before("@annotation(com.example.demo.aop.PermisionLog)")
	public void log (JoinPoint joinPoint)  {
//		
//		String className = joinPoint.getTarget().getClass().getSimpleName();
//		String methodName = joinPoint.getSignature().getName();
//		String methodSignature = className + "." + methodName + "()";

		System.err.println("*** entered To Annotaion' aspect!");
	
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String methodName = signature.getMethod().getName();
		Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
		Annotation[][] annotations = null;
		try {
			
			annotations = joinPoint.getTarget().getClass().getMethod(methodName,parameterTypes).getParameterAnnotations();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
//		for(Annotation[] a1 : annotations) {
//			for(Annotation a2 : a1) {
//				
//				System.err.println(a2.annotationType().get);
//			}
//		}
		System.err.println("*** out from permision aspect!");
		
//		Object[] args = joinPoint.getArgs();
//		for(Object arg : args) {
//			System.err.println(arg.toString());
//		}
		//Types[] permisions = 
//		try {
//			Object rv = joinPoint.proceed();
//			log.info(methodSignature + " - ended successfully");
//			return rv;
//		} catch (Throwable e) {
//			log.error(methodSignature + " - end with error" + e.getClass().getName());
//			throw e;
//		}
	}
}
