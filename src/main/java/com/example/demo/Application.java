package com.example.demo;

import java.util.Date;

import javax.lang.model.element.Element;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.element.ElementEntity;
import com.example.demo.element.ElementRepository;
import com.example.demo.element.ElementServiceJpa;
import com.example.demo.element.custom.ElementTypes;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserService;
import com.example.demo.user.UserServiceJPA;

@SpringBootApplication
public class Application {
//    private static String PLAYGROUND_NAME;
//
//    @Value("${playground.name}")
//    public void setSvnUrl(String name) {
//    	PLAYGROUND_NAME = name;
//    }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		System.err.println("sever is up");
	}

	@Bean
	public CommandLineRunner run(UserServiceJPA userService, ElementServiceJpa elementService) throws Exception {
		return args -> {

			try {
				 
//				 userRepository.save(new UserEntity("Shay@gmail.com", "playgrund", "username",
//				 "avatar", "Player"));
				
				
				 
//				 userRepository.save(new UserEntity("Shay@gmail.com", "playgrund", "username",
//				 "avatar", "Player"));
//
//			
//				 userRepository.save(new UserEntity("demo@gmail.com", "demo", "username",
//				 "avatar", "role"));
				 
			} catch (Exception e) {
				// TODO: handle exception
			}
		};
	}
}
