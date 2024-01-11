package io.ngocnhan_tran1996.code.example.database.support.oracle.in;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.SqlTypeValue;
import io.ngocnhan_tran1996.code.example.database.support.oracle.OracleValue;

public final class OracleTypeValue<T> extends OracleValue<T, OracleTypeValue<T>> {

  private List<T> values = new ArrayList<>();

  /** The type name of the ARRAY **/
  private final String arrayTypeName;

  private OracleTypeValue(Class<T> clazz, String arrayTypeName) {

    this.clazz = clazz;
    this.arrayTypeName = arrayTypeName;
  }

  public static OracleTypeValue<Object> withArray(String arrayTypeName) {

    return withArray(null, arrayTypeName);
  }

  public static <T> OracleTypeValue<T> withArray(Class<T> clazz, String arrayTypeName) {

    var oracleTypeValue = withTypeName(clazz, null);
    oracleTypeValue.withStructType(typeName);
    return oracleTypeValue;
  }

  public static <T> OracleTypeValue<T> withStruct(Class<T> clazz, String typeName) {

    var oracleTypeValue = withArray(clazz, ArrayTypeValue.STRUCT_TYPE);
    return oracleTypeValue.withTypeName(typeName);
  }

  public OracleTypeValue<T> value(T value) {

    this.values.add(value);
    return this;
  }

  public SqlTypeValue toTypeValue() {

    if (this.clazz == null
        || BeanUtils.isSimpleValueType(this.clazz)) {

      return new ArrayTypeValue<>(this.arrayTypeName, this.values);
    }

    var sqlTypeValue = ArrayTypeValue.STRUCT_TYPE.equals(this.arrayTypeName)
        ? new StructTypeValue<>(this.values, this.typeName)
        : new StructArrayTypeValue<>(this.arrayTypeName, this.values, this.typeName);

    return sqlTypeValue.setOracleMapper(this.getOracleMapper(this.clazz));
  }

}