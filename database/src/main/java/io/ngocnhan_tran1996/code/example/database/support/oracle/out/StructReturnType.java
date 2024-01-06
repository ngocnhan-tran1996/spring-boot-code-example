package io.ngocnhan_tran1996.code.example.database.support.oracle.out;

import java.sql.Types;
import lombok.Getter;

@Getter
class StructReturnType<T> extends StructArrayReturnType<T> {

  StructReturnType(String parameterName, String typeName, Class<T> clazz) {
    super(parameterName, typeName, clazz);
  }

  @Override
  protected int sqlType() {
    return Types.STRUCT;
  }

}