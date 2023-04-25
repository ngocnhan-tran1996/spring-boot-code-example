package com.springboot.code.example.database.jdbc.support.oracle.value;

import java.sql.Connection;
import java.sql.SQLException;
import com.springboot.code.example.database.jdbc.support.oracle.mapper.OracleMapper;
import com.springboot.code.example.database.jdbc.support.oracle.mapper.OracleStructMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OracleStructValue<T> extends OracleTypeValue {

  private final T source;

  /** The type name of the STRUCT **/
  private final String typeName;

  public static <T> OracleStructValue<T> add(T source, String typeName) {

    return new OracleStructValue<>(source, typeName);
  }

  @Override
  protected String getTypeName() {

    return this.typeName;
  }

  @Override
  protected Object createTypeValue(Connection connection, String typeName)
      throws SQLException {

    return this.getMapper()
        .toStruct(source, connection, typeName);
  }

  @Override
  protected OracleMapper getMapper() {

    return OracleStructMapper.newInstance(this.source.getClass());
  }

}