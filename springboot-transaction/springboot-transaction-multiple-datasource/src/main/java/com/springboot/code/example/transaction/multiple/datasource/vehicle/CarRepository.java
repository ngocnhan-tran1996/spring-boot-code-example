package com.springboot.code.example.transaction.multiple.datasource.vehicle;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<CarEntity, Integer> {

  @Query(value = """
      UPDATE user_transaction.CAR
      SET name = 'test',
      WHERE id = 2
      """,
      nativeQuery = true)
  int updateHistory();

  Integer deleteCarById(Integer id);

}