package com.springboot.code.example.database.jdbc;

import java.sql.Types;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import com.springboot.code.example.database.converter.PropertyConverter;
import com.springboot.code.example.database.jdbc.constant.JdbcConstant;
import com.springboot.code.example.database.jdbc.oracle.dto.OracleJdbcTemplateDto.PersonInput;
import com.springboot.code.example.database.jdbc.oracle.dto.OracleJdbcTemplateDto.PersonOuput;
import com.springboot.code.example.database.jdbc.oracle.dto.OracleJdbcTemplateDto.PersonSQLData;
import com.springboot.code.example.database.jdbc.support.oracle.value.OracleArrayValue;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OracleJdbcTemplateExecutor {

  private final JdbcTemplate jdbcTemplate;

  public Map<String, Object> executeProcedureWithSQLData() {

    return this.execute(
        OracleArrayValue.add(
            List.of(
                new PersonSQLData("Paul", "12"),
                new PersonSQLData("Victor", "13"))
                .toArray(PersonSQLData[]::new),
            JdbcConstant.PERSON_ARRAY));
  }

  public PersonOuput executeProcedureWithOracleArrayValue() {

    return PropertyConverter.convert(
        this.execute(
            OracleArrayValue.add(
                List.of(
                    new PersonInput("Harry", "15"),
                    new PersonInput("Judy", "16"))
                    .toArray(PersonInput[]::new),
                JdbcConstant.PERSON_RECORD,
                JdbcConstant.PERSON_ARRAY)),
        PersonOuput.class);
  }

  public Map<String, Object> executeProcedureWithObject() {

    return this.execute(
        OracleArrayValue.add(
            new Object[] {
                new Object[] {"Object", "1"},
                new Object[] {"Object", "2"}
            },
            JdbcConstant.PERSON_ARRAY));
  }

  private Map<String, Object> execute(Object obj) {

    jdbcTemplate.setResultsMapCaseInsensitive(true);
    var simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("PACK_EXAMPLE")
        .withProcedureName(JdbcConstant.CONCATENATE_TEXT_PROC);

    var parameters = new MapSqlParameterSource()
        .addValue(JdbcConstant.IN_NAME, "1")
        .addValue(JdbcConstant.IN_PERSONS, obj, Types.ARRAY)
        .addValue(JdbcConstant.EXPECT_OUTPUT_NUMBER_KEY, "1");

    return simpleJdbcCall.execute(parameters);
  }

}