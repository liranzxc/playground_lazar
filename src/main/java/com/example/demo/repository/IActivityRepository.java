package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.classes.entities.ActivityEntity;

public interface IActivityRepository extends CrudRepository<ActivityEntity, String> {

}
