package io.ngocnhan_tran1996.code.example.database.support.oracle.in;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.CollectionUtils;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.OracleTypeUtils;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.Strings;

class StructTypeValue<T> extends StructArrayTypeValue<T> {

  StructTypeValue(List<T> values, String structTypeName) {
    super(STRUCT_TYPE, values, structTypeName);
  }

  @Override
  protected Object createTypeValue(Connection connection, String typeName) throws SQLException {

    var uperCaseStructType = Strings.toUpperCase(typeName);
    var value = CollectionUtils.getByIndex(super.getValues(), 0);
    if (value == null) {

      OracleTypeUtils.throwMessage("Value must not be null!");
    }

    return this.getOracleMapper()
        .toStruct(value, connection, uperCaseStructType);
  }

}