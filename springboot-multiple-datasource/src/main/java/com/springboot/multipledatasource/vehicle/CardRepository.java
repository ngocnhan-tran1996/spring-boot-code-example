package com.springboot.multipledatasource.vehicle;

import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<CarEntity, Integer> {

}