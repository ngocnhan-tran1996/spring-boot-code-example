package com.springboot.code.example.database.jdbc.support.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Optional;
import com.springboot.code.example.common.helper.Strings;
import oracle.jdbc.driver.OracleConnection;

public class OracleArrayValue<T> extends OracleTypeValue {

  private final T[] values;

  /** The type name of the STRUCT **/
  private String structTypeName;

  /** The type name of the ARRAY **/
  private final String arrayTypeName;

  private OracleStructMapper oracleStructMapper;

  @SuppressWarnings("unchecked")
  public OracleArrayValue(
      T[] values,
      String arrayTypeName) {

    this.values = Optional.ofNullable(values)
        .orElse((T[]) new Object[0]);
    this.arrayTypeName = arrayTypeName;
  }

  public OracleArrayValue(
      T[] arrayValues,
      String structTypeName,
      String arrayTypeName) {

    this(arrayValues, arrayTypeName);
    this.structTypeName = structTypeName;
    this.oracleStructMapper = Optional.ofNullable(values)
        .filter(v -> v.length > 0)
        .map(v -> OracleStructMapper.newInstance(values[0].getClass()))
        .orElse(null);
  }

  @Override
  protected String getTypeName() {

    return this.arrayTypeName;
  }

  @Override
  protected Object createTypeValue(Connection connection, String typeName) throws SQLException {

    var oracleConnection = connection.unwrap(OracleConnection.class);
    if (Strings.isNotBlank(this.structTypeName)) {

      var length = this.values.length;
      Struct[] structValues = new Struct[length];
      for (int i = 0; i < length; i++) {
        structValues[i] = oracleStructMapper.toStruct(values[i], connection, this.structTypeName);
      }

      return oracleConnection
          .createOracleArray(typeName, structValues);
    }

    return oracleConnection
        .createOracleArray(typeName, this.values);
  }

}