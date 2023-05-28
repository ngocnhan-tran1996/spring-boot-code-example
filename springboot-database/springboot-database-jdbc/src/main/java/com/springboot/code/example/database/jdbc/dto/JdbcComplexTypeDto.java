package com.springboot.code.example.database.jdbc.dto;

import java.math.BigDecimal;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import com.springboot.code.example.common.helper.Strings;
import com.springboot.code.example.common.support.oracle.OracleColumn;
import com.springboot.code.example.database.jdbc.constant.JdbcComplexTypes;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JdbcComplexTypeDto {

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

    @Override
    public String toString() {
      return this.name + " and " + this.anotherAge;
    }

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
      return Strings.toUpperCase(JdbcComplexTypes.PERSON_RECORD);
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

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Car {

    private BigDecimal id;
    private String name;

    @Override
    public String toString() {
      return "id: " + id + " name: " + name;
    }
  }

}
