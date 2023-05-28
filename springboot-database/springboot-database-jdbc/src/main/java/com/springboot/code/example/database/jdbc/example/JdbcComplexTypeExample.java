package com.springboot.code.example.database.jdbc.example;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import com.springboot.code.example.common.converter.PropertyConverter;
import com.springboot.code.example.database.jdbc.constant.JdbcComplexTypes;
import com.springboot.code.example.database.jdbc.dto.JdbcComplexTypeDto.Car;
import com.springboot.code.example.database.jdbc.dto.JdbcComplexTypeDto.PersonInput;
import com.springboot.code.example.database.jdbc.dto.JdbcComplexTypeDto.PersonOuput;
import com.springboot.code.example.database.jdbc.dto.JdbcComplexTypeDto.PersonSQLData;
import com.springboot.code.example.database.jdbc.example.support.oracle.mapper.OracleStructMapper;
import com.springboot.code.example.database.jdbc.example.support.oracle.value.OracleArrayValue;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JdbcComplexTypeExample {

  private final JdbcTemplate jdbcTemplate;

  public Map<String, Object> executeProcedureWithSQLData() {

    return this.executeProcedure(
        OracleArrayValue.add(
            List.of(
                new PersonSQLData("Paul", "12"),
                new PersonSQLData("Victor", "13"))
                .toArray(PersonSQLData[]::new),
            JdbcComplexTypes.PERSON_ARRAY));
  }

  public PersonOuput executeProcedureWithOracleArrayValue() {

    return PropertyConverter.convert(
        this.executeProcedure(
            OracleArrayValue.add(
                List.of(
                    new PersonInput("Harry", "15"),
                    new PersonInput("Judy", "16"))
                    .toArray(PersonInput[]::new),
                JdbcComplexTypes.PERSON_RECORD,
                JdbcComplexTypes.PERSON_ARRAY)),
        PersonOuput.class);
  }

  public Map<String, Object> executeProcedureWithObject() {

    return this.executeProcedure(
        OracleArrayValue.add(
            new Object[] {
                new Object[] {"Object", "1"},
                new Object[] {"Object", "2"}
            },
            JdbcComplexTypes.PERSON_ARRAY));
  }

  private Map<String, Object> executeProcedure(Object obj) {

    jdbcTemplate.setResultsMapCaseInsensitive(true);
    var simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName(JdbcComplexTypes.PACK_EXAMPLE)
        .withProcedureName("CONCATENATE_TEXT_PROC");

    var parameters = new MapSqlParameterSource()
        .addValue("IN_NAME", "1")
        .addValue("IN_PERSONS", obj, Types.ARRAY)
        .addValue(JdbcComplexTypes.OUTPUT_NUMBER, "1");

    return simpleJdbcCall.execute(parameters);
  }

  @SuppressWarnings("unchecked")
  public List<Car> executeFunctionWithRowMapper() {

    jdbcTemplate.setResultsMapCaseInsensitive(true);
    var simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName(JdbcComplexTypes.PACK_EXAMPLE)
        .withFunctionName("CARD_INFO_FUNC")
        .returningResultSet(JdbcComplexTypes.RESULT, OracleStructMapper.newInstance(Car.class));

    return (List<Car>) simpleJdbcCall.execute(new MapSqlParameterSource())
        .get(JdbcComplexTypes.RESULT);
  }

  public Map<String, Object> executeFunctionWithInOutParameter() {

    jdbcTemplate.setResultsMapCaseInsensitive(true);
    var simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName(JdbcComplexTypes.PACK_EXAMPLE)
        .withFunctionName("PLUS_ONE_FUNC");

    var parameters = new MapSqlParameterSource()
        .addValue("IN_NUMBER", BigDecimal.ONE)
        .addValue(JdbcComplexTypes.OUTPUT_NUMBER, "1");
    return simpleJdbcCall.execute(parameters);
  }

  public List<PersonInput> executeFunctionWithTable() {

    var sql = String.format(
        "SELECT * FROM TABLE(%s.%s())",
        JdbcComplexTypes.PACK_EXAMPLE,
        "PERSON_INFO_FUNC");
    var namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    return namedParameterJdbcTemplate.query(
        sql,
        new MapSqlParameterSource(),
        OracleStructMapper.newInstance(PersonInput.class));
  }

}