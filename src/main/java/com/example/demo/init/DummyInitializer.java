package com.example.demo.init;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.demo.user.TypesEnumUser;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserService;
import com.example.demo.user.exceptions.EmailAlreadyRegisteredException;
import com.example.demo.user.exceptions.InvalidEmailException;
import com.example.demo.user.exceptions.InvalidRoleException;

//@Component
@Profile("presentation")
public class DummyInitializer {
	private UserService service;
	
	@Autowired
	public DummyInitializer(UserService service) {
		this.service = service;
	}
	
	@PostConstruct
	public void init() {
		String playground = "${playground.name}";
		String role = TypesEnumUser.Types.Manager.toString();
		String email = "playground@gmail.com";
		try {
			service.registerNewUser(new UserEntity(email, playground, "DemoManager" , "no avatar" , role));
		} catch (Exception e) {
			//do nothing
		}
	}
}
