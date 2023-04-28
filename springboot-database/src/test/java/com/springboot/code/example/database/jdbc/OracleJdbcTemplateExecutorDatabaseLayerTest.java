package com.springboot.code.example.database.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import com.springboot.code.example.database.jdbc.annotation.JdbcConfiguration;
import com.springboot.code.example.database.jdbc.constant.JdbcConstant;
import com.springboot.code.example.database.jdbc.oracle.dto.OracleJdbcTemplateDto.Car;
import com.springboot.code.example.database.jdbc.oracle.dto.OracleJdbcTemplateDto.PersonOuput;

@JdbcConfiguration
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class OracleJdbcTemplateExecutorDatabaseLayerTest {

  static final String outMsg =
      "IN_NAME = 1 AND IN_PARAMS =  NAME: %s AND AGE: %s AND  NAME: %s AND AGE: %s";

  @Autowired
  DataSource dataSource;

  @Autowired
  JdbcTemplate jdbcTemplate;

  OracleJdbcTemplateExecutor oracleJdbcTemplateExecutor;

  @BeforeEach
  void init() {
    oracleJdbcTemplateExecutor = new OracleJdbcTemplateExecutor(jdbcTemplate);
  }

  @Test
  void testExecuteProcedureWithOracleArrayValue() {

    // given
    var personOutput = new PersonOuput();
    personOutput.setOutMsg(String.format(outMsg, "Harry", "15", "Judy", "16"));
    personOutput.setNumber(BigDecimal.valueOf(2010));

    // then
    assertThat(oracleJdbcTemplateExecutor.executeProcedureWithOracleArrayValue())
        .usingRecursiveComparison()
        .isEqualTo(personOutput);
  }

  @Test
  void testExecuteProcedureWithSQLData() {

    // then
    assertThat(oracleJdbcTemplateExecutor.executeProcedureWithSQLData())
        .isEqualTo(Map.of(
            JdbcConstant.EXPECT_OUTPUT_KEY, String.format(outMsg, "Paul", "12", "Victor", "13"),
            JdbcConstant.EXPECT_OUTPUT_NUMBER_KEY, BigDecimal.valueOf(2010)));
  }

  @Test
  void testExecuteProcedureWithObject() {

    // then
    assertThat(oracleJdbcTemplateExecutor.executeProcedureWithObject())
        .isEqualTo(Map.of(
            JdbcConstant.EXPECT_OUTPUT_KEY, String.format(outMsg, "Object", "1", "Object", "2"),
            JdbcConstant.EXPECT_OUTPUT_NUMBER_KEY, BigDecimal.valueOf(2010)));
  }

  @Test
  void testExecuteFunctionWithRowMapper() {

    // given
    List<Car> cars = new ArrayList<>();
    cars.add(new Car(BigDecimal.ONE, "FORD"));
    cars.add(new Car(BigDecimal.valueOf(2), "HONDA"));
    cars.add(new Car(BigDecimal.valueOf(3), "HUYNDAI"));
    cars.add(new Car(BigDecimal.valueOf(4), "TOYOTA"));

    // then
    assertThat(oracleJdbcTemplateExecutor.executeFunctionWithRowMapper())
        .usingRecursiveComparison()
        .isEqualTo(cars);
  }

  @Test
  void testExecuteFunctionWithInOutParameter() {

    // then
    assertThat(oracleJdbcTemplateExecutor.executeFunctionWithInOutParameter())
        .isEqualTo(
            Map.of(
                JdbcConstant.EXPECT_OUTPUT_KEY, "Not negative number",
                JdbcConstant.EXPECT_OUTPUT_NUMBER_KEY, BigDecimal.ONE,
                "return", BigDecimal.valueOf(2)));
  }

}