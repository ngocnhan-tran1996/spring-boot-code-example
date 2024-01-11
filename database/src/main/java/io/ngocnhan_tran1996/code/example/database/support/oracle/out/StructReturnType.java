package io.ngocnhan_tran1996.code.example.database.support.oracle.out;

import java.sql.Types;

class StructReturnType<T> extends StructArrayReturnType<T> {

  @Override
  protected int sqlType() {
    return Types.STRUCT;
  }

}