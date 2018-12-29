package com.example.demo.aop;

import org.apache.commons.logging.Log;  // for class "Log"
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;  // @Aspect
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;  // @Component



@Component
@Aspect
public class LoggerAspect {

	private Log log = LogFactory.getLog(LoggerAspect.class);
	
	@Before("@annotation(com.example.demo.aop.ToLog)")
	public void log (JoinPoint joinPoint) {
		String className = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		System.err.println("*****************" + className + "." + methodName + "()");
	}
	
	
	@Around("@annotation(com.example.demo.aop.ToLog)")
	public Object log (ProceedingJoinPoint joinPoint) throws Throwable {
		String className = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		String methodSignature = className + "." + methodName + "()";

		
		try {
			Object rv = joinPoint.proceed();
			log.info(methodSignature + " - ended successfully");
			return rv;
		} catch (Throwable e) {
			log.error(methodSignature + " - end with error" + e.getClass().getName());
			throw e;
		}
	}
	
}
