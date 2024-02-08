package io.ngocnhan_tran1996.code.example.database.support.oracle.in;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlTypeValue;
import io.ngocnhan_tran1996.code.example.database.support.oracle.OracleValue;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.Strings;

public final class OracleTypeValue<T> extends OracleValue<OracleTypeValue<T>> {

  private List<T> values = new ArrayList<>();

  /** The type name of the ARRAY **/
  private final String arrayTypeName;
  private boolean allowNull;

  private OracleTypeValue(Class<T> clazz, String arrayTypeName) {

    this.clazz = clazz;
    this.arrayTypeName = arrayTypeName;
  }

  public static OracleTypeValue<Object> withArray(String arrayTypeName) {

    return withArray(null, arrayTypeName);
  }

  public static <T> OracleTypeValue<T> withArray(Class<T> clazz, String arrayTypeName) {

    return new OracleTypeValue<>(clazz, arrayTypeName);
  }

  public static <T> OracleTypeValue<T> withStruct(Class<T> clazz, String typeName) {

    var oracleTypeValue = withArray(clazz, StructTypeValue.STRUCT_TYPE);
    return oracleTypeValue.withTypeName(typeName);
  }

  public OracleTypeValue<T> allowNull() {

    this.allowNull = true;
    return this;
  }

  public OracleTypeValue<T> withParameterName(String parameterName) {

    super.parameterName = parameterName;
    return this;
  }

  public OracleTypeValue<T> value(T value) {

    if (this.allowNull
        && value == null) {

      return this;
    }

    this.values.add(value);
    return this;
  }

  public SqlTypeValue toTypeValue() {

    if (this.allowNull
        && this.values.isEmpty()) {

      return null;
    }

    if (this.clazz == null
        || BeanUtils.isSimpleValueType(this.clazz)) {

      return new ArrayTypeValue<>(this.arrayTypeName, this.values);
    }

    var sqlTypeValue = StructTypeValue.STRUCT_TYPE.equals(this.arrayTypeName)
        ? new StructTypeValue<>(this.values, this.typeName)
        : new StructArrayTypeValue<>(this.arrayTypeName, this.values, this.typeName);

    return sqlTypeValue.setOracleMapper(this.getOracleMapper(this.clazz));
  }

  public SqlInOutParameter toSqlInOutParameter() {

    var returnType = this.returnType();
    return new SqlInOutParameter(
        this.parameterName,
        returnType.sqlType(),
        Strings.toUpperCase(this.typeName),
        returnType);
  }

  @Override
  protected boolean isStructType() {

    return StructTypeValue.STRUCT_TYPE.equals(this.arrayTypeName);
  }

}