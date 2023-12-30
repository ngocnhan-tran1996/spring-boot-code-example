package com.springboot.code.example.transaction.domain;

import org.springframework.data.repository.CrudRepository;

public interface DogRepo extends CrudRepository<DogEntity, Integer> {

}