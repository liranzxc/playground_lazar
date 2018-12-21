package com.example.demo.activity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface ActivityRepository extends MongoRepository<ActivityEntity, String> {

}
