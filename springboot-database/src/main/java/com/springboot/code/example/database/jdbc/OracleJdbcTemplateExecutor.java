package com.springboot.code.example.database.jdbc;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import com.springboot.code.example.database.jdbc.support.oracle.OracleArrayValue;
import com.springboot.code.example.database.jdbc.support.oracle.OracleColumn;
import com.springboot.code.example.database.jdbc.support.oracle.PropertyMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@RequiredArgsConstructor
public class OracleJdbcTemplateExecutor {

  private final JdbcTemplate jdbcTemplate;

  public Map<String, Object> executeProcedureWithSQLData() {

    jdbcTemplate.setResultsMapCaseInsensitive(true);
    var jdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("PACK_EXAMPLE")
        .withProcedureName("P_CONCATENATE_TEXT");

    var parameters = new MapSqlParameterSource()
        .addValue("IN_NAME", "1")
        .addValue("IN_PERSONS", new OracleArrayValue<>(
            List.of(
                new PersonSQLData("Paul", "12"),
                new PersonSQLData("Victor", "13"))
                .toArray(PersonSQLData[]::new),
            "PACK_EXAMPLE.PERSON_ARRAY"),
            Types.ARRAY);

    return jdbcCall.execute(parameters);
  }

  public PersonOuput executeProcedureWithSQLData1() {

    jdbcTemplate.setResultsMapCaseInsensitive(true);
    var jdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("PACK_EXAMPLE")
        .withProcedureName("P_CONCATENATE_TEXT");

    var parameters = new MapSqlParameterSource()
        .addValue("IN_NAME", "1")
        .addValue("IN_PERSONS",
            new OracleArrayValue<>(
                List.of(
                    new PersonInput("Paul", "12"),
                    new PersonInput("Victor", "13"))
                    .toArray(PersonInput[]::new),
                "PACK_EXAMPLE.PERSON_RECORD",
                "PACK_EXAMPLE.PERSON_ARRAY"),
            Types.ARRAY);

    return PropertyMapper.map(jdbcCall.execute(parameters), PersonOuput.class);
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  static class PersonOuput {

    @OracleColumn(name = "OUT_MSG")
    private String outMsg;

    @Override
    public String toString() {
      return outMsg;
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  static class PersonInput {

    private String name;

    @OracleColumn(name = "age")
    private String anotherAge;

  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  static class PersonSQLData implements SQLData {

    private String name;
    private String value;

    @Override
    public String getSQLTypeName() throws SQLException {
      return "PACK_EXAMPLE.PERSON_RECORD";
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
      setName(stream.readString());
      setValue(stream.readString());
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
      stream.writeString(getName());
      stream.writeString(getValue());
    }
  }

}