package com.springboot.code.example.database.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import com.springboot.code.example.common.helper.Strings;
import com.springboot.code.example.database.jdbc.annotation.JdbcConfiguration;
import com.springboot.code.example.database.jdbc.constant.JdbcConstant;
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

  @InjectMocks
  OracleJdbcTemplateExecutor oracleJdbcTemplateExecutor;

  @BeforeEach
  public void init() throws Exception {

    initializeWithMetaData();
  }

  @Test
  void testExecuteProcedureWithSQLData() throws Exception {

    // then
    var actualOutput = oracleJdbcTemplateExecutor.executeProcedureWithSQLData();
    assertThat(actualOutput)
        .isEqualTo(EXPECT_OUTPUT);
  }

  @Test
  void testExecuteProcedureWithOracleArrayValue() throws Exception {

    var expectedOutput = new PersonOuput();
    expectedOutput.setNumber(BigDecimal.ONE);
    expectedOutput.setOutMsg(Strings.EMPTY);

    // then
    var actualOutput = oracleJdbcTemplateExecutor.executeProcedureWithOracleArrayValue();
    assertThat(actualOutput)
        .usingRecursiveComparison()
        .isEqualTo(expectedOutput);
  }

  @Test
  void testExecuteProcedureWithObject() throws Exception {

    // then
    var actualOutput = oracleJdbcTemplateExecutor.executeProcedureWithObject();
    assertThat(actualOutput)
        .isEqualTo(EXPECT_OUTPUT);
  }

  private void initializeWithMetaData() throws Exception {

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
            JdbcConstant.CONCATENATE_TEXT_PROC);
    doReturn(true, false)
        .when(proceduresResultSet)
        .next();
    doReturn(JdbcConstant.CONCATENATE_TEXT_PROC)
        .when(proceduresResultSet)
        .getString("PROCEDURE_CAT");

    doReturn(procedureColumnsResultSet)
        .when(databaseMetaData)
        .getProcedureColumns(JdbcConstant.PACK_EXAMPLE, JdbcConstant.USERNAME,
            JdbcConstant.CONCATENATE_TEXT_PROC, null);
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

    doReturn(databaseMetaData)
        .when(connection)
        .getMetaData();

    doReturn(EXPECT_OUTPUT)
        .when(jdbcTemplate)
        .call(any(), any());
  }

}