package com.springboot.code.example.database.support.oracle.out;

import org.springframework.jdbc.core.SqlOutParameter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class OracleReturnType<T> {

  private final Class<T> clazz;
  private final String parameterName;
  private String typeName;

  public static OracleReturnType<Object> withParameterName(String parameterName) {

    return new OracleReturnType<>(null, parameterName);
  }

  public static <T> OracleReturnType<T> withParameterName(Class<T> clazz, String parameterName) {

    return new OracleReturnType<>(clazz, parameterName);
  }

  public OracleReturnType<T> withTypeName(String typeName) {

    this.typeName = typeName;
    return this;
  }

  public SqlOutParameter toSqlOutParameter() {

    var out = clazz == null
        ? new ArrayReturnType(this.parameterName, this.typeName)
        : new StructArrayReturnType<T>(this.parameterName, this.typeName, clazz);
    return out.toSqlOutParameter();
  }

}