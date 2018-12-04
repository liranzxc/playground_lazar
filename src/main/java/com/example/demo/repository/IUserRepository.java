package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.classes.entities.UserEntity;

public interface IUserRepository extends MongoRepository<UserEntity, String> {
		
	public Optional<UserEntity> findByEmail(String email);
	public boolean existsByEmail(String email);
}
