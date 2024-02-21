package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.ChainedTransactionManagerConfig;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.OracleDataSourceConfig;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.PostgresDataSourceConfig;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("poly-datasource")
@SpringBootTest(classes = {PostgresDataSourceConfig.class, OracleDataSourceConfig.class,
    ChainedTransactionManagerConfig.class, UnexpectedRollbackTransaction.class})
class PolyJdbcTransactionTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Resource(name = "oracleJdbcTemplate")
    JdbcTemplate oracleJdbcTemplate;

    @Autowired
    UnexpectedRollbackTransaction unexpectedRollbackTransaction;

    @Test
    @Transactional(PostgresDataSourceConfig.TRANSACTION_MANAGER)
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
    @Transactional(OracleDataSourceConfig.TRANSACTION_MANAGER)
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

        List<Map<String, Object>> result = this.oracleJdbcTemplate.queryForList(
            "SELECT * FROM CAT");
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(List.of(Map.of(
                "ID", BigDecimal.ONE,
                "SPECIES", "Cat Changed")));
    }

    @Test
    @Transactional(OracleDataSourceConfig.TRANSACTION_MANAGER)
    void testSavePostgresWithOracleTransaction() {

        this.testSavePostgres();
    }

    @Test
    @Transactional(PostgresDataSourceConfig.TRANSACTION_MANAGER)
    void testSaveOracleWithPostgresTransaction() {

        this.testSaveOracle();
    }

    @Test
    @Transactional(OracleDataSourceConfig.TRANSACTION_MANAGER)
    void testSaveAllWithOracleTransaction() {

        this.testSaveOracle();
        this.testSavePostgres();
    }

    @Test
    @Transactional(PostgresDataSourceConfig.TRANSACTION_MANAGER)
    void testSaveAllWithPostgresTransaction() {

        this.testSavePostgres();
        this.testSaveOracle();
    }

    @Test
    @Transactional("chainedTransactionManager")
    void testSaveAllWithChainedTransaction() {

        this.testSavePostgres();
        this.testSaveOracle();
    }

    @Test
    @Transactional(value = "chainedTransactionManager", propagation = Propagation.NOT_SUPPORTED)
    void testSaveAllWithChainedTransactionWithException() {

        assertThatExceptionOfType(UnexpectedRollbackException.class)
            .isThrownBy(
                this.unexpectedRollbackTransaction::saveAllWithChainedTransactionWithException);
    }

}