package io.ngocnhan_tran1996.code.example.database.jdbc;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import io.ngocnhan_tran1996.code.example.database.dto.NamePrefixInput;
import io.ngocnhan_tran1996.code.example.database.dto.NamePrefixRecordInput;
import io.ngocnhan_tran1996.code.example.database.support.oracle.in.OracleTypeValue;
import io.ngocnhan_tran1996.code.example.database.support.oracle.out.OracleReturnType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class JdbcService {

  private final JdbcTemplate jdbcTemplate;

  Map<String, Object> excuteConcatNameProc(Object data) {

    jdbcTemplate.setResultsMapCaseInsensitive(true);
    var simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("jdbc_example_pack")
        .withProcedureName("concat_name_proc");

    var sqlParameterSource = new MapSqlParameterSource()
        .addValue("in_name", "Nhan")
        .addValue("in_names", data, Types.ARRAY);

    return simpleJdbcCall.execute(sqlParameterSource);
  }

  <T> Map<String, Object> executeComplexTypeOutProc(Class<T> clazz) {

    jdbcTemplate.setResultsMapCaseInsensitive(true);
    var simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("jdbc_example_pack")
        .withProcedureName("complex_type_out_proc")
        .declareParameters(
            OracleReturnType.withArrayParameterName(clazz, "out_names")
                .withTypeName("jdbc_example_pack.name_array")
                .toSqlOutParameter(),
            OracleReturnType.withArrayParameterName("out_numbers")
                .withTypeName("jdbc_example_pack.number_array")
                .toSqlOutParameter());

    return simpleJdbcCall.execute(new MapSqlParameterSource());
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

  Map<String, Object> executePlusOneFunc() {

    jdbcTemplate.setResultsMapCaseInsensitive(true);
    var simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("jdbc_example_pack")
        .withFunctionName("plus_one_func");

    var sqlParameterSource = new MapSqlParameterSource()
        .addValue("in_number", BigDecimal.ONE)
        .addValue("out_nbr", BigDecimal.TEN);

    return simpleJdbcCall.execute(sqlParameterSource);
  }

  /**
   * simpleJdbcCall can not call Function which has function pipeline format
   */
  void executeNameInfoTableFunc() {

    jdbcTemplate.setResultsMapCaseInsensitive(true);
    var simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("jdbc_example_pack")
        .withFunctionName("name_info_table_func")
        .declareParameters(
            OracleReturnType.withArrayParameterName(NamePrefixInput.class, "return")
                .withTypeName("jdbc_example_pack.name_array")
                .toSqlOutParameter());

    simpleJdbcCall.execute(new MapSqlParameterSource());
  }

  <T> T executeInOutObject(Class<T> clazz, T data) {

    jdbcTemplate.setResultsMapCaseInsensitive(true);
    var simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withSchemaName("user_nhan")
        .withCatalogName("jdbc_example_pack")
        .withProcedureName("in_out_object")
        .declareParameters(
            OracleReturnType.withStructParameterName(clazz, "obj")
                .withTypeName("user_nhan.name_object")
                .toSqlInOutParameter());

    var sqlParameterSource = new MapSqlParameterSource()
        .addValue("obj",
            OracleTypeValue.withStruct(clazz, "user_nhan.name_object")
                .value(data)
                .toTypeValue(),
            Types.STRUCT)
        .addValue("in_numbers",
            OracleTypeValue.withArray("user_nhan.jdbc_example_pack.number_array")
                .value(1)
                .value(2)
                .toTypeValue(),
            Types.ARRAY);

    return simpleJdbcCall.executeObject(clazz, sqlParameterSource);
  }
}