package com.springboot.code.example.database.poly.datasource.postgres;

import org.springframework.data.repository.CrudRepository;

public interface DogRepo extends CrudRepository<DogEntity, Integer> {

}