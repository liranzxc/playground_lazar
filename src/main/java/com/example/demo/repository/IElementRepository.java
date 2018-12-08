package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.classes.entities.ElementEntity;

public interface IElementRepository extends MongoRepository<ElementEntity, String> {

	public boolean existsByKey(String key);
	public Optional<ElementEntity> findByKey(String key);
	public void deleteByKey(String key);
	}
