package com.example.demo.Repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.classes.EntityClasses.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, String> {

}
