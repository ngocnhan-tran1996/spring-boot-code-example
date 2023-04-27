package com.springboot.code.example.database.entitymanager;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import com.springboot.code.example.database.entitymanager.annotation.EntityManagerConfiguration;
import com.springboot.code.example.database.entitymanager.pagination.EntityManagerPagination;
import com.springboot.code.example.database.entitymanager.pagination.Pagination;
import com.springboot.code.example.database.multiple.datasource.vehicle.CarEntity;

@EntityManagerConfiguration
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = "classpath:entitymanager/data.sql",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class EntityManagerExecutorDatabaseLayerTest {

  @Autowired
  EntityManager entityManager;

  @Test
  void testSaveAndFindAll() {

    // given
    entityManager.createNativeQuery(
        "INSERT INTO {h-schema}CAR(NAME) VALUES ('TEST')")
        .executeUpdate();

    String query = "SELECT id, name FROM {h-schema}CAR";

    @SuppressWarnings("unchecked")
    List<Tuple> tuples = entityManager.createNativeQuery(query, Tuple.class)
        .getResultList();

    // then
    assertThat(tuples).hasSize(5);
  }

  @Test
  void testPaginate() {

    // then
    assertThat(select(0, 0).getElements()).hasSize(4);
    assertThat(select(0, 20).getElements()).hasSize(4);
    assertThat(select(0, 2).getElements()).hasSize(2);
    assertThat(select(1, 2).getElements()).hasSize(2);
    assertThat(select(2, 2).getElements()).isEmpty();
    assertThat(select(3, 2).getElements()).isEmpty();
  }

  protected Pagination<CarEntity> select(int page, int size) {

    var entityManagerPagination = EntityManagerPagination.create(entityManager);
    return entityManagerPagination.query("SELECT id, name FROM {h-schema}CAR")
        .ofPageRequest(page, size)
        .getPages(CarEntity.class);
  }
}