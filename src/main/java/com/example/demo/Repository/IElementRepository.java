package com.example.demo.Repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.classes.EntityClasses.ElementEntity;

public interface IElementRepository extends CrudRepository<ElementEntity, String> {

}
