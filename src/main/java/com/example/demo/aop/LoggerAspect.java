package com.example.demo.aop;

import org.apache.commons.logging.Log;  // for class "Log"
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;

import org.aspectj.lang.annotation.Aspect;  // @Aspect
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;  // @Component


@Component
@Aspect
public class LoggerAspect {

	private Log log = LogFactory.getLog(LoggerAspect.class);
	
//	@Before("@annotation(com.example.demo.aop.MyLog)")
//	public void log (JoinPoint joinPoint) {
//		String className = joinPoint.getTarget().getClass().getSimpleName();
//		String methodName = joinPoint.getSignature().getName();
//		System.err.println("*****************" + className + "." + methodName + "()");
//	}
}
