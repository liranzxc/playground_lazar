package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.classes.entities.UserEntity;

public interface IUserRepository extends CrudRepository<UserEntity, String> {
		
}
