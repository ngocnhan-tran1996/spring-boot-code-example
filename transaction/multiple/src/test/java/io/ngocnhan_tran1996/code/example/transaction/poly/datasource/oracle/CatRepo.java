package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.oracle;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.OracleDataSourceConfig;

@Transactional(OracleDataSourceConfig.TRANSACTION_MANAGER)
public interface CatRepo extends CrudRepository<CatEntity, Integer> {

  @Modifying
  @Query(value = "INSERT INTO CAT VALUES (:id, :species)", nativeQuery = true)
  void insert(int id, String species);

  @Modifying
  @Query(value = "INSERT INTO CAT VALUES (2, 'Test');COMMIT", nativeQuery = true)
  void insert();

}