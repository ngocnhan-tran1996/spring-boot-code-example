package io.ngocnhan_tran1996.code.example.database.support.oracle.in;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.CollectionUtils;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.OracleTypeUtils;

class StructTypeValue<T> extends StructArrayTypeValue<T> {

  StructTypeValue(List<T> values, String structTypeName) {
    super(STRUCT_TYPE, values, structTypeName);
  }

  @Override
  protected Object createTypeValue(Connection connection, String typeName) throws SQLException {

    var value = OracleTypeUtils.throwIfNull(CollectionUtils.getByIndex(super.getValues(), 0));
    return this.getOracleMapper()
        .toStruct(connection, value, this.getStructTypeName());
  }

}