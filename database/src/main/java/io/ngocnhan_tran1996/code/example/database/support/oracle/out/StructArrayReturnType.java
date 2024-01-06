package io.ngocnhan_tran1996.code.example.database.support.oracle.out;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import io.ngocnhan_tran1996.code.example.database.support.oracle.mapper.DelegateOracleMapper;
import io.ngocnhan_tran1996.code.example.database.support.oracle.mapper.OracleMapper;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.OracleTypeUtils;
import lombok.Getter;
import oracle.jdbc.OracleStruct;

@Getter
class StructArrayReturnType<T> extends ArrayReturnType {

  private final Class<T> clazz;
  private OracleMapper<T> oracleMapper;

  StructArrayReturnType(String parameterName, String typeName, Class<T> clazz) {

    super(parameterName, typeName);
    this.clazz = clazz;
    this.oracleMapper = DelegateOracleMapper.get(clazz);
  }

  @Override
  protected Object convertArray(Array array) throws SQLException {

    List<T> values = new ArrayList<>();
    var structValues = (Object[]) array.getArray();

    for (Object object : structValues) {

      if (object instanceof Struct struct) {

        values.add(this.convertStruct(struct));
        continue;
      }

      String errorMsg = object == null
          ? null
          : object.getClass().getName();
      OracleTypeUtils.throwMessage(String.format("Expected STRUCT but got '%s'", errorMsg));
    }

    // type array for easier converting
    return values.toArray();
  }

  @Override
  protected T convertStruct(Struct struct) throws SQLException {

    return this.getOracleMapper().fromStruct((OracleStruct) struct);
  }

}