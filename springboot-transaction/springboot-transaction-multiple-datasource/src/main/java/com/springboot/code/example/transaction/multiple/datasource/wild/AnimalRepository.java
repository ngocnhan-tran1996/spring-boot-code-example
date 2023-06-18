package com.springboot.code.example.transaction.multiple.datasource.wild;

import org.springframework.data.repository.CrudRepository;

public interface AnimalRepository extends CrudRepository<AnimalEntity, Integer> {

  Integer deleteAnimalById(Integer id);

}