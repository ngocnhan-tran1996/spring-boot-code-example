package com.springboot.code.example.database.jdbc.oracle.dto;

import java.math.BigDecimal;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import com.springboot.code.example.database.jdbc.constant.JdbcConstant;
import com.springboot.code.example.database.jdbc.support.oracle.OracleColumn;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OracleJdbcTemplateDto {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PersonOuput {

    @OracleColumn(name = "OUT_MSG")
    private String outMsg;

    @OracleColumn(name = "out_nbR")
    private BigDecimal number;

    @Override
    public String toString() {
      return this.outMsg + " and " + this.number;
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PersonInput {

    private String name;

    @OracleColumn(name = "age")
    private String anotherAge;

  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PersonSQLData implements SQLData {

    private String name;
    private String value;

    @Override
    public String getSQLTypeName() throws SQLException {
      return JdbcConstant.PERSON_RECORD;
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