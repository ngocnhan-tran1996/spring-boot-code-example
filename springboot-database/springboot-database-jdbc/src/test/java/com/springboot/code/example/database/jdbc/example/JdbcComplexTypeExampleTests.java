package com.springboot.code.example.database.jdbc.example;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedCaseInsensitiveMap;
import com.springboot.code.example.database.jdbc.dto.JdbcComplexTypeObjects.Car;
import com.springboot.code.example.database.jdbc.dto.JdbcComplexTypeObjects.PersonInput;
import com.springboot.code.example.database.jdbc.dto.JdbcComplexTypeObjects.PersonOuput;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JdbcComplexTypeExampleTests {

  @Autowired
  JdbcTemplate jdbcTemplate;

  JdbcComplexTypeExample jdbcComplexTypeExample;

  @BeforeEach
  void init() {

    jdbcComplexTypeExample = new JdbcComplexTypeExample(jdbcTemplate);
  }

  @Test
  void testExecuteProcedureWithSQLData() {

    // given
    Map<String, Object> expectOutput = new LinkedCaseInsensitiveMap<>(2);
    expectOutput.put(
        "OUT_MSG",
        "IN_NAME = 1 AND IN_PARAMS =  NAME: Paul AND AGE: 12 AND  NAME: Victor AND AGE: 13");
    expectOutput.put("OUT_NBR", BigDecimal.valueOf(2010));

    var actualOutput = jdbcComplexTypeExample.executeProcedureWithSQLData();

    // then
    assertThat(actualOutput)
        .usingRecursiveComparison()
        .isEqualTo(expectOutput);
  }

  @Test
  void testExecuteProcedureWithOracleArrayValue() {

    // given
    PersonOuput expectOutput = new PersonOuput(
        "IN_NAME = 1 AND IN_PARAMS =  NAME: Harry AND AGE: 15 AND  NAME: Judy AND AGE: 16",
        BigDecimal.valueOf(2010));

    var actualOutput = jdbcComplexTypeExample.executeProcedureWithOracleArrayValue();

    // then
    assertThat(actualOutput)
        .usingRecursiveComparison()
        .isEqualTo(expectOutput);
  }

  @Test
  void testExecuteProcedureWithObject() {

    // given
    Map<String, Object> expectOutput = new LinkedCaseInsensitiveMap<>(2);
    expectOutput.put(
        "OUT_MSG",
        "IN_NAME = 1 AND IN_PARAMS =  NAME: Object AND AGE: 1 AND  NAME: Object AND AGE: 2");
    expectOutput.put("OUT_NBR", BigDecimal.valueOf(2010));

    var actualOutput = jdbcComplexTypeExample.executeProcedureWithObject();

    // then
    assertThat(actualOutput)
        .usingRecursiveComparison()
        .isEqualTo(expectOutput);
  }

  @Test
  @Sql("data.sql")
  void testExecuteFunctionWithRowMapper() {

    // given
    List<Car> expectOutput = List.of(
        new Car(BigDecimal.ONE, "TEST 1"),
        new Car(BigDecimal.valueOf(2), "TEST 2"),
        new Car(BigDecimal.valueOf(3), "TEST 3"),
        new Car(BigDecimal.valueOf(4), "TEST 4"),
        new Car(BigDecimal.valueOf(5), "TEST 5"));

    var actualOutput = jdbcComplexTypeExample.executeFunctionWithRowMapper();

    // then
    assertThat(actualOutput)
        .usingRecursiveComparison()
        .isEqualTo(expectOutput);
  }

  @Test
  void testExecuteFunctionWithInOutParameter() {

    // given
    Map<String, Object> expectOutput = new LinkedCaseInsensitiveMap<>(2);
    expectOutput.put("OUT_MSG", "Not negative number");
    expectOutput.put("OUT_NBR", BigDecimal.ONE);
    expectOutput.put("return", BigDecimal.valueOf(2));

    var actualOutput = jdbcComplexTypeExample.executeFunctionWithInOutParameter();

    // then
    assertThat(actualOutput)
        .usingRecursiveComparison()
        .isEqualTo(expectOutput);
  }

  @Test
  @Sql("data.sql")
  void testExecuteFunctionWithTable() {

    // given
    List<PersonInput> expectOutput = List.of(
        new PersonInput("1", "TEST 1"),
        new PersonInput("2", "TEST 2"),
        new PersonInput("3", "TEST 3"),
        new PersonInput("4", "TEST 4"),
        new PersonInput("5", "TEST 5"));

    var actualOutput = jdbcComplexTypeExample.executeFunctionWithTable();

    // then
    assertThat(actualOutput)
        .usingRecursiveComparison()
        .isEqualTo(expectOutput);
  }

  @Test
  void testExecuteOutputComplexTypeProcedure() {

    // given
    List<PersonInput> personInputs = List.of(
        new PersonInput("0", "0"),
        new PersonInput("1", "1"),
        new PersonInput("2", "2"),
        new PersonInput("3", "3"),
        new PersonInput("4", "4"),
        new PersonInput("5", "5"));

    Map<String, Object> expectOutput = new LinkedCaseInsensitiveMap<>(2);
    expectOutput.put("out_numbers", new Object[] {
        BigDecimal.ZERO,
        BigDecimal.ONE,
        BigDecimal.valueOf(2),
        BigDecimal.valueOf(3),
        BigDecimal.valueOf(4),
        BigDecimal.valueOf(5)});
    expectOutput.put("OUT_PERSONS", personInputs.toArray());
    var actualOutput = jdbcComplexTypeExample.executeOutputComplexTypeProcedure();

    // then
    assertThat(actualOutput)
        .usingRecursiveComparison()
        .isEqualTo(expectOutput);
  }

}