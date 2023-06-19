package com.springboot.code.example.transaction.jpa.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

@SpringBootTest
class JpaExampleTests {

  @Autowired
  JpaExample jpaExample;

  @Test
  void testInsertHistory() {

    assertThatThrownBy(jpaExample::insertHistoryWithoutTransaction)
        .isInstanceOf(InvalidDataAccessApiUsageException.class);
  }

  @Test
  void testInsertHistoryWithoutTransactioninsertHistoryWithoutTransaction() {

    assertThatThrownBy(jpaExample::insertHistoryWithoutTransaction)
        .isInstanceOf(InvalidDataAccessApiUsageException.class);
  }

  @Test
  void testInsertHistoryWithTransaction() {

    assertThat(jpaExample.insertHistoryWithTransaction())
        .isEqualTo(1);
  }

}