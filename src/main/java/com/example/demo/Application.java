package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.user.UserEntity;
import com.example.demo.user.UserRepository;

@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		System.err.println("sever is up");
	}

	@Bean
	public CommandLineRunner run(UserRepository userRepository) throws Exception {
		return args -> {

			//userRepository.save(new UserEntity("lirannh@gmail.com", "playground_lazar", "username", "avatar", "Player"));
			//userRepository.save(new UserEntity("Shay@gmail.com", "playgrund", "username", "avatar", "Player"));

			//userRepository.save(new UserEntity("demo@gmail.com", "demo", "username", "avatar", "role"));
		};
	}
}
