package com.springboot.code.example.transaction.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class JdbcTests {

  @Autowired
  JdbcTemplate jdbcTemplate;

  private static final String TABLE_NAME = "ANIMATION";

  @Test
  void testInsert() {

    JdbcTestUtils.deleteFromTables(jdbcTemplate, TABLE_NAME);
    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, TABLE_NAME))
        .isZero();
    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, TABLE_NAME))
        .isEqualTo(1);
  }

}