package io.ngocnhan_tran1996.code.example.database.support.oracle.in;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import io.ngocnhan_tran1996.code.example.database.support.oracle.mapper.DelegateOracleMapper;
import io.ngocnhan_tran1996.code.example.database.support.oracle.mapper.OracleMapper;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.OracleTypeUtils;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.Strings;
import lombok.Getter;
import oracle.jdbc.driver.OracleConnection;

@Getter
class StructArrayTypeValue<T> extends ArrayTypeValue<T> {

  StructArrayTypeValue(String arrayTypeName, List<T> values, String structTypeName) {
    super(arrayTypeName, values);
    this.structTypeName = OracleTypeUtils.throwIfBlank(structTypeName);
  }

  StructArrayTypeValue<T> setOracleMapper(Class<T> clazz) {

    this.oracleMapper = DelegateOracleMapper.get(clazz);
    return this;
  }

  /** The type name of the STRUCT **/
  private final String structTypeName;

  private OracleMapper<T> oracleMapper;

  @Override
  protected Object createTypeValue(Connection connection, String typeName) throws SQLException {

    var uperCaseStructType = Strings.toUpperCase(this.structTypeName);
    List<Struct> structs = new ArrayList<>(super.getValues().size());

    for (var value : super.getValues()) {

      var struct = this.getOracleMapper().toStruct(value, connection, uperCaseStructType);
      structs.add(struct);
    }

    return connection
        .unwrap(OracleConnection.class)
        .createOracleArray(typeName, structs.toArray(Struct[]::new));
  }

}