package com.example.demo.aop;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.example.demo.user.TypesEnumUser.Types;

@Retention(RUNTIME)
@Target(METHOD)
public @interface UserPermission {	
	
	Types[] permissions() default {};
	
}
