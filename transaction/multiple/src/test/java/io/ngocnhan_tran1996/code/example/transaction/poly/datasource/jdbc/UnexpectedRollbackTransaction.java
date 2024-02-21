package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.jdbc;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@TestConfiguration
class UnexpectedRollbackTransaction {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Resource(name = "oracleJdbcTemplate")
    JdbcTemplate oracleJdbcTemplate;

    @Transactional(value = "chainedTransactionManager")
    void saveAllWithChainedTransactionWithException() {

        // reset all
        this.jdbcTemplate.update("DELETE FROM DOG");
        this.oracleJdbcTemplate.update("DELETE FROM CAT");

        // create
        this.jdbcTemplate.update(
            "INSERT INTO DOG (id, species) VALUES (?, ?)",
            1, "Dog 1");
        this.jdbcTemplate.update(
            "INSERT INTO DOG (id, species) VALUES (?, ?)",
            2, "Dog 2");
        this.oracleJdbcTemplate.update(
            "INSERT INTO CAT (id, species) VALUES (?, ?)",
            1, "Cat 1");
        this.oracleJdbcTemplate.update(
            "INSERT INTO CAT (id, species) VALUES (?, ?)",
            2, "Cat 2");

        // update
        this.oracleJdbcTemplate.update(
            "UPDATE CAT SET species = ? WHERE id = ?",
            "Cat Changed", 1);

        // delete
        this.oracleJdbcTemplate.update("DELETE FROM CAT WHERE id = ?", 2);

        // intend to throw exception
        this.oracleJdbcTemplate.update("INSERT INTO CAT VALUES (2, 'Test');COMMIT");
    }

}