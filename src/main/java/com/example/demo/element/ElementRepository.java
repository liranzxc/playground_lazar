package com.example.demo.element;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ElementRepository extends MongoRepository<ElementEntity, String> {

	public boolean existsByKey(String key);
	
	
	public void deleteByKey(String key);
	
	public Optional<ElementEntity> findByKey(String key);
	public Optional<ElementEntity> findAllByKeyAndExpireDateGreaterThanOrExpireDateIsNull(String key, Date expireDate);

	public List<ElementEntity> findAllByExpireDateGreaterThanOrExpireDateIsNull(Date expireDate);
	public List<ElementEntity> findAllByExpireDateGreaterThanOrExpireDateIsNull(Date expireDate, Pageable pageable);
	
	public List<ElementEntity> findAllByName(String name, Pageable pageable);
	public List<ElementEntity> findAllByNameAndExpireDateGreaterThanOrExpireDateIsNull(String name, Date expireDate, Pageable pageable);


	public List<ElementEntity> findAllByType(String type, Pageable pageable);
	public List<ElementEntity> findAllByTypeAndExpireDateGreaterThanOrExpireDateIsNull(String type, Date expireDate, Pageable pageable);


	}
