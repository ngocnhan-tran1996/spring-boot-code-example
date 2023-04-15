package com.springboot.code.example.database.jdbc;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import com.springboot.code.example.database.jdbc.constant.JdbcConstant;

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

}