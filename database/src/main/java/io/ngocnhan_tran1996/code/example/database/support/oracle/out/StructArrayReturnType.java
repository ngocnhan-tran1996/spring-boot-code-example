package io.ngocnhan_tran1996.code.example.database.support.oracle.out;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import io.ngocnhan_tran1996.code.example.database.support.oracle.exception.OracleTypeException;

public class StructArrayReturnType<T> extends ArrayReturnType<T> {

  @Override
  protected Object convertArray(Connection connection, Array array) throws SQLException {

    var objects = (Object[]) array.getArray();
    List<T> values = new ArrayList<>(objects.length);

    for (Object object : objects) {

      if (object instanceof Struct struct) {

        values.add(this.convertStruct(connection, struct));
        continue;
      }

      String errorMsg = object == null
          ? null
          : object.getClass().getName();
      throw new OracleTypeException(String.format("Expected STRUCT but got '%s'", errorMsg));
    }

    // type array for easier converting
    return values.toArray();
  }

  @SuppressWarnings("unchecked")
  @Override
  protected T convertStruct(Connection connection, Struct struct) throws SQLException {

    return (T) super.getOracleMapper().fromStruct(connection, struct);
  }

}