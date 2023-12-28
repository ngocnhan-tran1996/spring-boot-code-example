package com.springboot.code.example.database.support.oracle.out;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import com.springboot.code.example.database.support.oracle.mapper.OracleMapper;
import com.springboot.code.example.database.support.oracle.mapper.PojoMapper;
import com.springboot.code.example.database.support.oracle.mapper.RecordMapper;
import com.springboot.code.example.database.support.oracle.utils.OracleTypeUtils;
import lombok.Getter;
import oracle.jdbc.OracleStruct;

@Getter
class StructArrayReturnType<T> extends ArrayReturnType {

  private static final String RECORD_CLASS_NAME = "java.lang.Record";

  private final Class<T> clazz;
  private OracleMapper<T> oracleMapper;

  StructArrayReturnType(String parameterName, String typeName, Class<T> clazz) {

    super(parameterName, typeName);
    this.clazz = clazz;
  }

  @Override
  protected Object convertArray(Array array) throws SQLException {

    List<T> values = new ArrayList<>();

    boolean isRecord = clazz != null
        && clazz.getSuperclass() != null
        && RECORD_CLASS_NAME.equals(clazz.getSuperclass().getName());
    var mapper = isRecord
        ? RecordMapper.newInstance(clazz)
        : PojoMapper.newInstance(clazz);

    var structValues = (Object[]) array.getArray();
    for (Object struct : structValues) {

      if (struct instanceof Struct) {

        values.add(mapper.fromStruct((OracleStruct) struct));
        continue;
      }

      String errorMsg = struct == null
          ? null
          : struct.getClass().getName();
      OracleTypeUtils.throwMessage(String.format("Expected STRUCT but got '%s'", errorMsg));
    }

    return values.toArray();
  }

}