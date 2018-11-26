package com.example.demo.Repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.classes.EntityClasses.ActivityEntity;

public interface IActivityRepository extends CrudRepository<ActivityEntity, String> {

}
