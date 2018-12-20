package com.example.demo.user;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, String> {
		
	public Optional<UserEntity> findByEmail(String email);
	public boolean existsByEmail(String email);
	
}
