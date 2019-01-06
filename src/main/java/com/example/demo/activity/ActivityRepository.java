package com.example.demo.activity;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActivityRepository extends MongoRepository<ActivityEntity, String> {
	
	public boolean existsByKey(String key);
	public List<ActivityEntity> findByType(String type, Pageable pageable);
}
