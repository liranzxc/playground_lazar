package com.example.demo.element;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ElementRepository extends MongoRepository<ElementEntity, String> {

	public boolean existsByKey(String key);
	public Optional<ElementEntity> findByKey(String key);
	public void deleteByKey(String key);
	public List<ElementEntity> findByName(String name, Pageable pageable);
	public List<ElementEntity> findByType(String type, Pageable pageable);
	
	
	}
