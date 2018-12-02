package com.example.demo;

import org.apache.catalina.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.classes.entities.UserEntity;
import com.example.demo.repository.IUserRepository;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		System.err.println("AVIV LAZAR SHOLET");
		System.err.println("SHAY is noob aviv = best");
		System.err.println("SHAY is noob aviv = best");

	}

	 @Bean
	    public CommandLineRunner run(IUserRepository userRepository) throws Exception {
	        return args -> {
	           
	        	userRepository.save(new UserEntity("lirannh@gmail.com",
	        			"playgrund", "username", "avatar", "role"));
	        	userRepository.save(new UserEntity("Shay@gmail.com",
	        			"playgrund", "username", "avatar", "role"));
	        };
	    }
}
