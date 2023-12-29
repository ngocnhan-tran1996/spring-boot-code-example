package com.springboot.code.example.database.poly.datasource.oracle;

import org.springframework.data.repository.CrudRepository;

public interface CatRepo extends CrudRepository<CatEntity, Integer> {

}