package com.springboot.code.example.database.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
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
import com.springboot.code.example.database.constant.Strings;
import com.springboot.code.example.database.jdbc.annotation.JdbcConfiguration;
import com.springboot.code.example.database.jdbc.constant.JdbcConstant;

@JdbcConfiguration
@ExtendWith(MockitoExtension.class)
class JdbcTemplateExecutorTest {

  private static final String EXPECT_OUTPUT_VALUE = "Table already exists";
  public static final String EXPECT_OUTPUT_KEY = "OUT_MSG";
  public static final Map<String, Object> EXPECT_OUTPUT = Map.of(
      EXPECT_OUTPUT_KEY, EXPECT_OUTPUT_VALUE);

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
  JdbcTemplateExecutor jdbcTemplateExecutor;

  @BeforeEach
  public void init() throws Exception {

    initializeWithMetaData();
  }

  @Test
  void testExecuteProcedureWithDataSource() throws Exception {

    // when
    doReturn(EXPECT_OUTPUT_VALUE)
        .when(callableStatement)
        .getObject(3);
    doReturn(-1)
        .when(callableStatement)
        .getUpdateCount();

    // then
    var actualOutput = jdbcTemplateExecutor.executeProcedureWithDataSource();
    assertThat(actualOutput)
        .isEqualTo(EXPECT_OUTPUT);
  }

  @Test
  void testExecuteProcedureWithJdbcTemplate() throws Exception {

    // when
    doReturn(dataSource)
        .when(jdbcTemplate)
        .getDataSource();
    doReturn(EXPECT_OUTPUT)
        .when(jdbcTemplate)
        .call(any(), any());

    lenient()
        .doReturn(1)
        .when(callableStatement)
        .getUpdateCount();

    // then
    var actualOutput = jdbcTemplateExecutor.executeProcedureWithJdbcTemplate();
    assertThat(actualOutput)
        .isEqualTo(EXPECT_OUTPUT);
  }

  private void initializeWithMetaData() throws Exception {

    doReturn(connection)
        .when(dataSource)
        .getConnection();

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
        .getProcedures(Strings.EMPTY, JdbcConstant.USERNAME, JdbcConstant.PROC_NAME);
    doReturn(true, false)
        .when(proceduresResultSet)
        .next();
    doReturn(JdbcConstant.PROC_NAME)
        .when(proceduresResultSet)
        .getString("PROCEDURE_CAT");

    doReturn(procedureColumnsResultSet)
        .when(databaseMetaData)
        .getProcedureColumns(Strings.EMPTY, JdbcConstant.USERNAME, JdbcConstant.PROC_NAME, null);
    doReturn(true, true, true, false)
        .when(procedureColumnsResultSet)
        .next();
    doReturn(JdbcConstant.IN_SCHEMA, JdbcConstant.IN_TABLE_NAME, EXPECT_OUTPUT_KEY)
        .when(procedureColumnsResultSet)
        .getString("COLUMN_NAME");
    doReturn(1, 1, 4)
        .when(procedureColumnsResultSet)
        .getInt("COLUMN_TYPE");

    doReturn(databaseMetaData)
        .when(connection)
        .getMetaData();
    lenient()
        .doReturn(callableStatement)
        .when(connection)
        .prepareCall(String.format("{call %s(?, ?, ?)}", JdbcConstant.PROC_NAME));
  }

}