package com.springboot.code.example.database.jdbc;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

@Service
public class JdbcTemplateExecutor {

  private SimpleJdbcCall simpleJdbcCall;

  public JdbcTemplateExecutor(DataSource dataSource) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.setResultsMapCaseInsensitive(true);
    this.simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withSchemaName("system")
        .withProcedureName("p_create_car_tbl");
  }

  public Map<String, Object> executeProcedure() {

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("in_schema", "system")
        .addValue("in_table_name", "car");

    return this.simpleJdbcCall.execute(parameters);
  }

}
