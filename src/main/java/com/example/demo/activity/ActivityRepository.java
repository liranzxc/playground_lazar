package com.example.demo.activity;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActivityRepository extends MongoRepository<ActivityEntity, String> {
	
	public boolean existsByKey(String key);

}
