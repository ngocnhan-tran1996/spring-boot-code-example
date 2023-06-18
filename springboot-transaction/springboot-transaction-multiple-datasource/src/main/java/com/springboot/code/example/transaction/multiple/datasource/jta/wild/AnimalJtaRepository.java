package com.springboot.code.example.transaction.multiple.datasource.jta.wild;

import org.springframework.data.repository.CrudRepository;

public interface AnimalJtaRepository extends CrudRepository<AnimalJtaEntity, Integer> {

  Integer deleteAnimalById(Integer id);

}