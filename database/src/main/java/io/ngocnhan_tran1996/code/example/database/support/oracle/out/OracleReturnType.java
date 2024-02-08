package io.ngocnhan_tran1996.code.example.database.support.oracle.out;

import org.springframework.jdbc.core.SqlOutParameter;
import io.ngocnhan_tran1996.code.example.database.support.oracle.OracleValue;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.Strings;

public final class OracleReturnType<T> extends OracleValue<OracleReturnType<T>> {

  private boolean isStructType = false;

  private OracleReturnType(Class<T> clazz, String parameterName) {

    this.clazz = clazz;
    super.parameterName = parameterName;
  }

  public static OracleReturnType<Object> withArray(String parameterName) {

    return withArray(null, parameterName);
  }

  public static <T> OracleReturnType<T> withArray(Class<T> clazz, String parameterName) {

    return new OracleReturnType<>(clazz, parameterName);
  }

  public static <T> OracleReturnType<T> withStruct(Class<T> clazz, String parameterName) {

    var oracleReturnType = withArray(clazz, parameterName);
    oracleReturnType.isStruct();
    return oracleReturnType;
  }

  private void isStruct() {

    this.isStructType = true;
  }

  public SqlOutParameter toSqlOutParameter() {

    var returnType = this.returnType();
    return new SqlOutParameter(
        this.parameterName,
        returnType.sqlType(),
        Strings.toUpperCase(this.typeName),
        returnType);
  }

  @Override
  protected boolean isStructType() {

    return this.isStructType;
  }

}