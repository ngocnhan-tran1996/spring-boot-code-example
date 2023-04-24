package com.springboot.code.example.database.jdbc.support.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.driver.OracleConnection;

@RequiredArgsConstructor
public class OracleArrayValue<T> extends OracleTypeValue {

  private final T[] values;

  /** The type name of the STRUCT **/
  private String structTypeName;

  /** The type name of the ARRAY **/
  private final String arrayTypeName;

  public OracleArrayValue(
      T[] arrayValues,
      String structTypeName,
      String arrayTypeName) {

    this(arrayValues, arrayTypeName);
    this.structTypeName = structTypeName;
  }

  @Override
  protected String getTypeName() {

    return this.arrayTypeName;
  }

  @Override
  protected Object createTypeValue(Connection connection, String typeName) throws SQLException {

    var oracleConnection = connection.unwrap(OracleConnection.class);
    if (this.structTypeName != null) {

      var length = this.values.length;
      Struct[] structValues = new Struct[length];
      for (int i = 0; i < length; i++) {
        var mapper = BeanPropertyStructMapper.newInstance(values[i].getClass());
        structValues[i] = mapper.toStruct(values[i], connection, this.structTypeName);
      }

      return oracleConnection
          .createOracleArray(typeName, structValues);
    }

    return oracleConnection
        .createOracleArray(typeName, this.values);
  }

}