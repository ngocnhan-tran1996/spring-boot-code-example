package com.springboot.code.example.database.jdbc;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import com.springboot.code.example.database.converter.PropertyConverter;
import com.springboot.code.example.database.jdbc.constant.JdbcConstant;
import com.springboot.code.example.database.jdbc.oracle.dto.OracleJdbcTemplateDto.Car;
import com.springboot.code.example.database.jdbc.oracle.dto.OracleJdbcTemplateDto.PersonInput;
import com.springboot.code.example.database.jdbc.oracle.dto.OracleJdbcTemplateDto.PersonOuput;
import com.springboot.code.example.database.jdbc.oracle.dto.OracleJdbcTemplateDto.PersonSQLData;
import com.springboot.code.example.database.jdbc.oracle.dto.OracleJdbcTemplateDto.PersonTable;
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
        .withCatalogName(JdbcConstant.PACK_EXAMPLE)
        .withProcedureName(JdbcConstant.CONCATENATE_TEXT_PROC);

    var parameters = new MapSqlParameterSource()
        .addValue(JdbcConstant.IN_NAME, "1")
        .addValue(JdbcConstant.IN_PERSONS, obj, Types.ARRAY)
        .addValue(JdbcConstant.EXPECT_OUTPUT_NUMBER_KEY, "1");

    return simpleJdbcCall.execute(parameters);
  }

  @SuppressWarnings("unchecked")
  public List<Car> executeFunctionWithRowMapper() {

    jdbcTemplate.setResultsMapCaseInsensitive(true);
    var simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName(JdbcConstant.PACK_EXAMPLE)
        .withFunctionName(JdbcConstant.CARD_INFO_FUNC)
        .returningResultSet(JdbcConstant.RESULT, BeanPropertyRowMapper.newInstance(Car.class));

    return (List<Car>) simpleJdbcCall.execute(new MapSqlParameterSource())
        .get(JdbcConstant.RESULT);
  }

  public Map<String, Object> executeFunctionWithInOutParameter() {

    jdbcTemplate.setResultsMapCaseInsensitive(true);
    var simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName(JdbcConstant.PACK_EXAMPLE)
        .withFunctionName(JdbcConstant.PLUS_ONE_FUNC);

    var parameters = new MapSqlParameterSource()
        .addValue("IN_NUMBER", BigDecimal.ONE)
        .addValue(JdbcConstant.EXPECT_OUTPUT_NUMBER_KEY, "1");
    return simpleJdbcCall.execute(parameters);
  }

  public List<PersonTable> executeFunctionWithTable() {

    var parameters = new MapSqlParameterSource()
        .addValue("in_name", "Nhan")
        .addValue("in_age", "18");

    var namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    return namedParameterJdbcTemplate.query(
        "SELECT name, age FROM TABLE(PACK_EXAMPLE.DUAL_INFO_FUNC(:in_name,:in_age))",
        parameters,
        BeanPropertyRowMapper.newInstance(PersonTable.class));
  }

}