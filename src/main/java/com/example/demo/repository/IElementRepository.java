package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.classes.entities.ElementEntity;

public interface IElementRepository extends MongoRepository<ElementEntity, String> {

}
