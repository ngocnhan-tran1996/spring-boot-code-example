package com.springboot.multipledatasource.wild;

import org.springframework.data.repository.CrudRepository;

public interface AnimalRepository extends CrudRepository<AnimalEntity, Integer> {

}