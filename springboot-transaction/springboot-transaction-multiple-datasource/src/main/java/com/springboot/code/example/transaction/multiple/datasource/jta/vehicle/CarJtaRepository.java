package com.springboot.code.example.transaction.multiple.datasource.jta.vehicle;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CarJtaRepository extends CrudRepository<CarJtaEntity, Integer> {

  @Query(value = """
      UPDATE user_transaction.CAR_JTA
      SET name = 'test',
      WHERE id = 2
      """,
      nativeQuery = true)
  int updateHistory();

  Integer deleteCarById(Integer id);

}