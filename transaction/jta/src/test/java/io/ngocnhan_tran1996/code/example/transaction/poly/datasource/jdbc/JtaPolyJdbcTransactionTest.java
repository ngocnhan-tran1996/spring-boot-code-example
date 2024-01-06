package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.JtaOracleDataSourceConfig;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.JtaPostgresDataSourceConfig;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.TransactionManagerConfig;
import jakarta.annotation.Resource;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@ActiveProfiles("poly-datasource")
@SpringBootTest(classes = {
    JtaPostgresDataSourceConfig.class,
    JtaOracleDataSourceConfig.class,
    JtaUnexpectedRollbackTransaction.class,
    TransactionManagerConfig.class})
class JtaPolyJdbcTransactionTest {

  @Resource(name = "jtaPostgresJdbcTemplate")
  JdbcTemplate jdbcTemplate;

  @Resource(name = "jtaOracleJdbcTemplate")
  JdbcTemplate oracleJdbcTemplate;

  @Autowired
  JtaUnexpectedRollbackTransaction unexpectedRollbackTransaction;

  @Test
  @Transactional(value = "jtaTransactionManager")
  void testSavePostgres() {

    // reset all
    this.jdbcTemplate.update("DELETE FROM DOG");
    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "dog"))
        .isZero();

    // create
    this.jdbcTemplate.update(
        "INSERT INTO DOG (id, species) VALUES (?, ?)",
        1, "Dog 1");
    this.jdbcTemplate.update(
        "INSERT INTO DOG (id, species) VALUES (?, ?)",
        2, "Dog 2");

    // read
    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "dog"))
        .isEqualTo(2);

    // update
    this.jdbcTemplate.update(
        "UPDATE DOG SET species = ? WHERE id = ?",
        "Dog Changed", 1);

    // delete
    this.jdbcTemplate.update("DELETE FROM DOG WHERE id = ?", 2);

    List<Map<String, Object>> result = this.jdbcTemplate.queryForList("SELECT * FROM dog");
    assertThat(result)
        .isEqualTo(List.of(Map.of(
            "id", 1,
            "species", "Dog Changed")));
  }

  @Test
  @Transactional(value = "jtaTransactionManager")
  void testSaveOracle() {

    // reset all
    this.oracleJdbcTemplate.update("DELETE FROM CAT");
    assertThat(JdbcTestUtils.countRowsInTable(this.oracleJdbcTemplate, "CAT"))
        .isZero();

    // create
    this.oracleJdbcTemplate.update(
        "INSERT INTO CAT (id, species) VALUES (?, ?)",
        1, "Cat 1");
    this.oracleJdbcTemplate.update(
        "INSERT INTO CAT (id, species) VALUES (?, ?)",
        2, "Cat 2");

    // read
    assertThat(JdbcTestUtils.countRowsInTable(this.oracleJdbcTemplate, "CAT"))
        .isEqualTo(2);

    // update
    this.oracleJdbcTemplate.update(
        "UPDATE CAT SET species = ? WHERE id = ?",
        "Cat Changed", 1);

    // delete
    this.oracleJdbcTemplate.update("DELETE FROM CAT WHERE id = ?", 2);

    List<Map<String, Object>> result = this.oracleJdbcTemplate.queryForList("SELECT * FROM CAT");
    assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(List.of(Map.of(
            "ID", BigDecimal.ONE,
            "SPECIES", "Cat Changed")));
  }

  @Test
  @Transactional(value = "jtaTransactionManager")
  void testSaveAllWithJtaTransaction() {

    this.testSavePostgres();
    this.testSaveOracle();
  }

  @Test
  @Transactional(value = "jtaTransactionManager")
  void testSaveAllWithJtaTransactionWithException() {

    assertThatExceptionOfType(UncategorizedSQLException.class)
        .isThrownBy(this.unexpectedRollbackTransaction::saveAllWithChainedTransactionWithException);
  }

}