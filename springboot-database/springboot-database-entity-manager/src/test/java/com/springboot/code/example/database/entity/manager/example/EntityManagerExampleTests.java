package com.springboot.code.example.database.entity.manager.example;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import com.springboot.code.example.database.entity.manager.entity.CarEntity;
import com.springboot.code.example.database.entity.manager.response.CarResponse;

@DataJpaTest
@Sql(scripts = "data.sql")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class EntityManagerExampleTests {

  @Autowired
  TestEntityManager testEntityManager;

  EntityManagerExample entityManagerExample;

  @BeforeEach
  void init() {

    // given
    entityManagerExample = new EntityManagerExample(testEntityManager.getEntityManager());
  }

  @Test
  void testConvertTypedQueryToNativeQuery() {

    // given
    List<CarResponse> expectOutput = List.of(
        new CarResponse(1, "TEST 1"),
        new CarResponse(2, "TEST 2"),
        new CarResponse(3, "TEST 3"),
        new CarResponse(4, "TEST 4"),
        new CarResponse(5, "TEST 5"));

    var actualOutput = entityManagerExample.convertTypedQueryToNativeQuery();

    // then
    assertThat(actualOutput)
        .hasSize(5)
        .usingRecursiveFieldByFieldElementComparator()
        .isEqualTo(expectOutput);
  }

  @Test
  void testSaveAndFindAll() {

    // given
    List<CarEntity> expectOutput = List.of(
        new CarEntity(1, "TEST 1"),
        new CarEntity(2, "TEST 2"),
        new CarEntity(3, "TEST 3"),
        new CarEntity(4, "TEST 4"),
        new CarEntity(5, "TEST 5"));

    // then
    assertThat(entityManagerExample.saveAndFindAll())
        .hasSize(6)
        .usingRecursiveFieldByFieldElementComparator()
        .containsAll(expectOutput);
  }

}