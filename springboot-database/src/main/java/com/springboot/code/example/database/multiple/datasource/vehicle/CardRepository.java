package com.springboot.code.example.database.multiple.datasource.vehicle;

import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<CarEntity, Integer> {

}