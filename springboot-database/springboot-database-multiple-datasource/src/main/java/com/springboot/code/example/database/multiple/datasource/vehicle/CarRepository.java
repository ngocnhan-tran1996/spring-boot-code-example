package com.springboot.code.example.database.multiple.datasource.vehicle;

import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<CarEntity, Integer> {

  Integer deleteCarById(Integer id);

}