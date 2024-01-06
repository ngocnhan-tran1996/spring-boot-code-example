package io.ngocnhan_tran1996.code.example.database.support.oracle.out;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Types;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ArrayReturnType extends AbstractSqlReturnType {

  private final String parameterName;
  private final String typeName;

  @Override
  protected Object convertArray(Array array) throws SQLException {

    return array.getArray();
  }

  @Override
  protected ArrayReturnType getHandler() {
    return this;
  }

  @Override
  protected int sqlType() {

    return Types.ARRAY;
  }

  @Override
  protected Object convertStruct(Struct struct) throws SQLException {
    return null;
  }

}