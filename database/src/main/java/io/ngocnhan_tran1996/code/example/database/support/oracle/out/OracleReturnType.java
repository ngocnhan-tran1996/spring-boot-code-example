package io.ngocnhan_tran1996.code.example.database.support.oracle.out;

import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import io.ngocnhan_tran1996.code.example.database.support.oracle.OracleValue;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.Strings;

public final class OracleReturnType<T> extends OracleValue<T, OracleReturnType<T>> {

  private final String parameterName;
  private boolean isStructType = false;

  private OracleReturnType(Class<T> clazz, String parameterName) {

    this.clazz = clazz;
    this.parameterName = parameterName;
  }

  public static OracleReturnType<Object> withArrayParameterName(String parameterName) {

    return withArrayParameterName(null, parameterName);
  }

  public static <T> OracleReturnType<T> withArrayParameterName(Class<T> clazz,
      String parameterName) {

    return new OracleReturnType<>(clazz, parameterName);
  }

  public static <T> OracleReturnType<T> withStructParameterName(Class<T> clazz,
      String parameterName) {

    var oracleReturnType = withArrayParameterName(clazz, parameterName);
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

  public SqlInOutParameter toSqlInOutParameter() {

    var returnType = this.returnType();
    return new SqlInOutParameter(
        this.parameterName,
        returnType.sqlType(),
        Strings.toUpperCase(this.typeName),
        returnType);
  }

  protected AbstractSqlReturnType<T> returnType() {

    if (this.clazz == null
        || BeanUtils.isSimpleValueType(this.clazz)) {

      return new ArrayReturnType<>();
    }

    return isStructType
        ? new StructReturnType<T>()
            .setOracleMapper(this.getOracleMapper(this.clazz))
        : new StructArrayReturnType<T>()
            .setOracleMapper(this.getOracleMapper(this.clazz));
  }

}