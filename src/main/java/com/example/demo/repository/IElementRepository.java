package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.classes.entities.ElementEntity;

public interface IElementRepository extends CrudRepository<ElementEntity, String> {

}
