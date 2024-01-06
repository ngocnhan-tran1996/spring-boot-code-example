package io.ngocnhan_tran1996.code.example.database.support.oracle.out;

import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class OracleReturnType<T> {

  private final Class<T> clazz;
  private final String parameterName;
  private String typeName;
  private boolean isStructType = false;

  public static OracleReturnType<Object> withParameterName(String parameterName) {

    return withParameterName(null, parameterName);
  }

  public static <T> OracleReturnType<T> withParameterName(Class<T> clazz, String parameterName) {

    return new OracleReturnType<>(clazz, parameterName);
  }

  public OracleReturnType<T> withTypeName(String typeName) {

    this.typeName = typeName;
    return this;
  }

  public OracleReturnType<T> structType() {

    this.isStructType = true;
    return this;
  }

  public SqlOutParameter toSqlOutParameter() {

    return returnType().toSqlOutParameter();
  }

  public SqlInOutParameter toSqlInOutParameter() {

    return returnType().toSqlInOutParameter();
  }

  protected AbstractSqlReturnType returnType() {

    if (this.clazz == null) {

      return new ArrayReturnType(this.parameterName, this.typeName);
    }

    return isStructType
        ? new StructReturnType<>(this.parameterName, this.typeName, clazz)
        : new StructArrayReturnType<>(this.parameterName, this.typeName, clazz);
  }

}