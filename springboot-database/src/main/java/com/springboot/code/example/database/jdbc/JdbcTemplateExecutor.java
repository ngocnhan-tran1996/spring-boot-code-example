package com.springboot.code.example.database.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.AbstractSqlTypeValue;
import org.springframework.stereotype.Service;
import com.springboot.code.example.database.jdbc.constant.JdbcConstant;
import oracle.jdbc.driver.OracleConnection;

@Service
public class JdbcTemplateExecutor {

  private SimpleJdbcCall simpleJdbcCall;
  private JdbcTemplate jdbcTemplate;

  public JdbcTemplateExecutor(DataSource dataSource, JdbcTemplate jdbcTemplate) {

    var template = new JdbcTemplate(dataSource);
    template.setResultsMapCaseInsensitive(true);
    this.simpleJdbcCall = new SimpleJdbcCall(template)
        .withProcedureName(JdbcConstant.PROC_NAME);

    this.jdbcTemplate = jdbcTemplate;
    jdbcTemplate.setResultsMapCaseInsensitive(true);
  }

  public Map<String, Object> executeProcedureWithDataSource() {

    return this.simpleJdbcCall.execute(parameters());
  }

  public Map<String, Object> executeProcedureWithJdbcTemplate() {

    var jdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withProcedureName(JdbcConstant.PROC_NAME);

    return jdbcCall.execute(parameters());
  }

  private static SqlParameterSource parameters() {

    return new MapSqlParameterSource()
        .addValue(JdbcConstant.IN_SCHEMA, JdbcConstant.USERNAME)
        .addValue(JdbcConstant.IN_TABLE_NAME, "car");
  }

  public Map<String, Object> executeProcedureWith() {

    var jdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("PACK_EXAMPLE")
        .withProcedureName("P_CONCATENATE_TEXT")
        .withNamedBinding();

    var parameters = new MapSqlParameterSource()
        .addValue("IN_NAME", "1")
        .addValue("IN_PARAMS", new AbstractSqlTypeValue() {

          @Override
          protected Object createTypeValue(Connection con, int sqlType, String typeName)
              throws SQLException {

            return con.unwrap(OracleConnection.class).createARRAY(
                "PACK_EXAMPLE.IN_PARAMS",
                new Object[] {
                    new Object[] {"1", "TEST"},
                    new Object[] {"1", "TEST 2"}
                });
          }
        },
            Types.ARRAY,
            "PACK_EXAMPLE.IN_PARAMS");


    System.out.println("test " + jdbcCall.execute(parameters));
    return Map.of();
  }


}