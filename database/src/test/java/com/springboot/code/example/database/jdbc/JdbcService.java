package com.springboot.code.example.database.jdbc;

import java.sql.Types;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import com.springboot.code.example.database.domain.name.NamePrefixRecordInput;
import com.springboot.code.example.database.support.oracle.input.OracleArrayValue;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class JdbcService {

  private final JdbcTemplate jdbcTemplate;

  Map<String, Object> excuteConcatNameProc() {

    var data = OracleArrayValue.withTypeName("jdbc_example_pack.name_array")
        .data(new Object[] {"Nhan", "Ngoc"})
        .data(new Object[] {"Ngoc", "Nhan"});

    jdbcTemplate.setResultsMapCaseInsensitive(true);
    var simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("jdbc_example_pack")
        .withProcedureName("concat_name_proc");

    var sqlParameterSource = new MapSqlParameterSource()
        .addValue("in_name", "Nhan")
        .addValue("in_names", data, Types.ARRAY);

    return simpleJdbcCall.execute(sqlParameterSource);
  }

  @SuppressWarnings("unchecked")
  List<NamePrefixRecordInput> executeNameInfoFunc() {

    jdbcTemplate.setResultsMapCaseInsensitive(true);
    var simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("jdbc_example_pack")
        .withFunctionName("name_info_func")
        .returningResultSet("return", DataClassRowMapper.newInstance(NamePrefixRecordInput.class));

    return (List<NamePrefixRecordInput>) simpleJdbcCall.execute(new MapSqlParameterSource())
        .get("return");
  }

}