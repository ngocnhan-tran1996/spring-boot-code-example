package com.springboot.code.example.transaction.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.container.EnableTestcontainers;
import com.springboot.code.example.container.PostgreSQLContainerInitializer;

@ActiveProfiles("postgres")
@SpringBootTest
@EnableTestcontainers(PostgreSQLContainerInitializer.class)
@TestMethodOrder(OrderAnnotation.class)
class JdbcTest {

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Test
  @Order(0)
  void testDeleteAll() {

    this.jdbcTemplate.update("DELETE FROM DOG");
    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "dog"))
        .isZero();
  }

  @Test
  @Order(1)
  @Transactional
  void testInsertWithTransaction() {

    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "dog"))
        .isZero();
    this.jdbcTemplate.update(
        "INSERT INTO DOG (id, species) VALUES (?, ?)",
        1, "Chihuahua");
    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "dog"))
        .isEqualTo(1);
  }

  @Test
  @Order(2)
  void testInsertWithoutTransaction() {

    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "dog"))
        .isZero();
    this.jdbcTemplate.update(
        "INSERT INTO DOG (id, species) VALUES (?, ?)",
        1, "Chihuahua");
    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "dog"))
        .isEqualTo(1);
  }

  @Test
  @Order(3)
  @Transactional
  void testUpdateWithTransaction() {

    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "dog"))
        .isEqualTo(1);

    this.jdbcTemplate.update(
        "UPDATE DOG SET species = ?",
        "Anana 1");

    List<Map<String, Object>> result = this.jdbcTemplate.queryForList("SELECT * FROM dog");
    assertThat(result)
        .isEqualTo(List.of(Map.of(
            "id", 1,
            "species", "Anana 1")));
  }

  @Test
  @Order(4)
  void testUpdateWithoutTransaction() {

    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "dog"))
        .isEqualTo(1);

    List<Map<String, Object>> result = this.jdbcTemplate.queryForList("SELECT * FROM dog");
    assertThat(result)
        .isEqualTo(List.of(Map.of(
            "id", 1,
            "species", "Chihuahua")));

    this.jdbcTemplate.update(
        "UPDATE DOG SET species = ?",
        "Anana 1");

    List<Map<String, Object>> result2 = this.jdbcTemplate.queryForList("SELECT * FROM dog");
    assertThat(result2)
        .isEqualTo(List.of(Map.of(
            "id", 1,
            "species", "Anana 1")));
  }

  @Test
  @Order(5)
  @Transactional
  void testDeleteWithTransaction() {

    List<Map<String, Object>> result = this.jdbcTemplate.queryForList("SELECT * FROM dog");

    assertThat(result)
        .isEqualTo(List.of(Map.of(
            "id", 1,
            "species", "Anana 1")));

    this.jdbcTemplate.update("DELETE FROM DOG");
    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "dog"))
        .isZero();
  }

  @Test
  @Order(6)
  void testDeleteWithoutTransaction() {

    List<Map<String, Object>> result = this.jdbcTemplate.queryForList("SELECT * FROM dog");

    assertThat(result)
        .isEqualTo(List.of(Map.of(
            "id", 1,
            "species", "Anana 1")));

    this.jdbcTemplate.update("DELETE FROM DOG");
    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "dog"))
        .isZero();
  }

  @Test
  @Order(7)
  void testCount() {

    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "dog"))
        .isZero();
  }

}