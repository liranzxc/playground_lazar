package com.example.demo.element;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ElementRepository extends MongoRepository<ElementEntity, String> {

	public boolean existsByKey(String key);
	public Optional<ElementEntity> findByKey(String key);
	public void deleteByKey(String key);
	
	}
