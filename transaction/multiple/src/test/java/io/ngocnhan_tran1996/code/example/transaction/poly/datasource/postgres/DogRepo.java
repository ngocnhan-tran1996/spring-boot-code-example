package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.postgres;

import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.PostgresDataSourceConfig;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(PostgresDataSourceConfig.TRANSACTION_MANAGER)
public interface DogRepo extends CrudRepository<DogEntity, Integer> {

    @Modifying
    @Query(value = "INSERT INTO DOG VALUES (:id, :species)", nativeQuery = true)
    void insert(int id, String species);

}