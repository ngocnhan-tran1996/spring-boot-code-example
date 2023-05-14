package com.springboot.code.example.database.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import jakarta.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import com.springboot.code.example.common.helper.Strings;
import com.springboot.code.example.database.jdbc.annotation.JdbcConfiguration;
import com.springboot.code.example.database.jdbc.constant.JdbcConstant;
import com.springboot.code.example.database.jdbc.oracle.dto.OracleJdbcTemplateDto.Car;
import com.springboot.code.example.database.jdbc.oracle.dto.OracleJdbcTemplateDto.PersonInput;
import com.springboot.code.example.database.jdbc.oracle.dto.OracleJdbcTemplateDto.PersonOuput;

@JdbcConfiguration
@ExtendWith(MockitoExtension.class)
class OracleJdbcTemplateExecutorTest {

  public static final Map<String, Object> EXPECT_OUTPUT = Map.of(
      JdbcConstant.EXPECT_OUTPUT_NUMBER_KEY, "1",
      JdbcConstant.EXPECT_OUTPUT_KEY, Strings.EMPTY);

  @Mock
  Connection connection;

  @Mock
  DataSource dataSource;

  @Mock
  DatabaseMetaData databaseMetaData;

  @Mock
  CallableStatement callableStatement;

  @Mock
  ResultSet proceduresResultSet;

  @Mock
  ResultSet procedureColumnsResultSet;

  @Mock
  JdbcTemplate jdbcTemplate;

  @Mock
  PreparedStatement preparedStatement;

  @InjectMocks
  OracleJdbcTemplateExecutor oracleJdbcTemplateExecutor;

  @Test
  void testExecuteProcedureWithSQLData() throws Exception {

    // when
    this.initializeWithProcedureMetaData();

    // then
    var actualOutput = oracleJdbcTemplateExecutor.executeProcedureWithSQLData();
    assertThat(actualOutput)
        .isEqualTo(EXPECT_OUTPUT);
  }

  @Test
  void testExecuteProcedureWithOracleArrayValue() throws Exception {

    // given
    var expectedOutput = new PersonOuput();
    expectedOutput.setNumber(BigDecimal.ONE);
    expectedOutput.setOutMsg(Strings.EMPTY);

    // when
    this.initializeWithProcedureMetaData();

    // then
    var actualOutput = oracleJdbcTemplateExecutor.executeProcedureWithOracleArrayValue();
    assertThat(actualOutput)
        .usingRecursiveComparison()
        .isEqualTo(expectedOutput);
  }

  @Test
  void testExecuteProcedureWithObject() throws Exception {

    // when
    this.initializeWithProcedureMetaData();

    // then
    var actualOutput = oracleJdbcTemplateExecutor.executeProcedureWithObject();
    assertThat(actualOutput)
        .isEqualTo(EXPECT_OUTPUT);
  }

  private void initializeWithProcedureMetaData() throws Exception {

    this.initializeWithMetaData(JdbcConstant.CONCATENATE_TEXT_PROC, EXPECT_OUTPUT);

    doReturn(true, true, true, true, false)
        .when(procedureColumnsResultSet)
        .next();
    doReturn(
        JdbcConstant.IN_NAME,
        JdbcConstant.IN_PERSONS,
        JdbcConstant.EXPECT_OUTPUT_NUMBER_KEY,
        JdbcConstant.EXPECT_OUTPUT_KEY)
            .when(procedureColumnsResultSet)
            .getString("COLUMN_NAME");
    doReturn(1, 1, 2, 4)
        .when(procedureColumnsResultSet)
        .getInt("COLUMN_TYPE");
  }

  @Test
  void testExecuteFunctionWithRowMapper() throws Exception {

    // given
    var expectOutput = List.of(
        new Car(BigDecimal.ONE, "FORD"));

    // when
    this.initializeWithMetaData(
        JdbcConstant.CARD_INFO_FUNC,
        Map.of(JdbcConstant.RESULT, expectOutput));

    // then
    assertThat(oracleJdbcTemplateExecutor.executeFunctionWithRowMapper())
        .usingRecursiveComparison()
        .isEqualTo(expectOutput);
  }

  @Test
  void testExecuteFunctionWithInOutParameter() throws Exception {

    // given
    var expectOutput = Map.of(JdbcConstant.RESULT, BigDecimal.ONE);

    // when
    this.initializeWithMetaData(
        JdbcConstant.PLUS_ONE_FUNC,
        expectOutput);

    // then
    assertThat(oracleJdbcTemplateExecutor.executeFunctionWithInOutParameter())
        .usingRecursiveComparison()
        .isEqualTo(expectOutput);
  }

  @SuppressWarnings("unchecked")
  @Test
  void testExecuteFunctionWithTable() throws Exception {

    // given
    var expectedOutput = List.of(new PersonInput("Nhan", "18"));

    // when
    doReturn(expectedOutput)
        .when(jdbcTemplate)
        .query((PreparedStatementCreator) any(), (RowMapper<PersonInput>) any());

    // then
    assertThat(oracleJdbcTemplateExecutor.executeFunctionWithTable())
        .isEqualTo(expectedOutput);
  }

  private void initializeWithMetaData(
      String procedureName,
      Object expectedOutput)
      throws Exception {

    doReturn(connection)
        .when(dataSource)
        .getConnection();

    doReturn(dataSource)
        .when(jdbcTemplate)
        .getDataSource();

    doReturn(true)
        .when(databaseMetaData)
        .storesUpperCaseIdentifiers();
    doReturn("Oracle")
        .when(databaseMetaData)
        .getDatabaseProductName();
    doReturn(JdbcConstant.USERNAME)
        .when(databaseMetaData)
        .getUserName();

    doReturn(proceduresResultSet)
        .when(databaseMetaData)
        .getProcedures(
            JdbcConstant.PACK_EXAMPLE,
            JdbcConstant.USERNAME,
            procedureName);
    doReturn(true, false)
        .when(proceduresResultSet)
        .next();
    doReturn(procedureName)
        .when(proceduresResultSet)
        .getString("PROCEDURE_CAT");

    doReturn(procedureColumnsResultSet)
        .when(databaseMetaData)
        .getProcedureColumns(
            JdbcConstant.PACK_EXAMPLE,
            JdbcConstant.USERNAME,
            procedureName,
            null);

    doReturn(databaseMetaData)
        .when(connection)
        .getMetaData();

    doReturn(expectedOutput)
        .when(jdbcTemplate)
        .call(any(), any());
  }

}