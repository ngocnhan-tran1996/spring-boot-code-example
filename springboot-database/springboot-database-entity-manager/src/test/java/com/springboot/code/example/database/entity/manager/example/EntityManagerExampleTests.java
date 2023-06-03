package com.springboot.code.example.database.entity.manager.example;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

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

    // then
    assertThat(entityManagerExample.convertTypedQueryToNativeQuery()).hasSize(5);
  }

  @Test
  void testSaveAndFindAll() {

    // then
    assertThat(entityManagerExample.saveAndFindAll()).hasSize(6);
  }

}