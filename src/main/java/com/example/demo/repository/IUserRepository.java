package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.classes.entities.UserEntity;

public interface IUserRepository extends MongoRepository<UserEntity, String> {
		
}
