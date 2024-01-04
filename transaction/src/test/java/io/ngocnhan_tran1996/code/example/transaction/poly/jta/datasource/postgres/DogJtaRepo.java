package io.ngocnhan_tran1996.code.example.transaction.poly.jta.datasource.postgres;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional("jtaTransactionManager")
public interface DogJtaRepo extends CrudRepository<DogJtaEntity, Integer> {

  @Modifying
  @Query(value = "INSERT INTO DOG VALUES (:id, :species)", nativeQuery = true)
  void insert(int id, String species);

}