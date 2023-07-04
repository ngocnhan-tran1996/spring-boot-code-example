package com.springboot.code.example.database.jdbc.support.oracle;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.SqlOutParameter;
import com.springboot.code.example.common.helper.Strings;
import com.springboot.code.example.database.jdbc.support.oracle.mapper.OracleMapper;
import com.springboot.code.example.database.jdbc.support.oracle.mapper.OracleStructMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.OracleStruct;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OracleStructArrayReturnType<T> extends AbstractSqlReturnType {

  private final OracleMapper<T> oracleMapper;

  @Override
  protected Object convertArray(Array array) throws SQLException {

    List<T> values = new ArrayList<>();

    var structValues = (Object[]) array.getArray();
    for (int x = 0; x < structValues.length; x++) {

      Object struct = structValues[x];
      if (struct instanceof Struct) {

        values.add(oracleMapper.fromStruct((OracleStruct) struct));
      } else {

        String errorMsg = struct == null
            ? "'null'"
            : struct.getClass().getName();
        throw new OracleTypeValueException("Expected STRUCT but got " + errorMsg);
      }
    }

    return values.toArray();
  }

  public static <T> SqlOutParameter toSqlOutParameter(
      String outParameterName,
      String arrayTypeName,
      Class<T> mappedClass) {

    return convertSqlOutParameter(
        outParameterName,
        Strings.toUpperCase(arrayTypeName),
        new OracleStructArrayReturnType<>(OracleStructMapper.newInstance(mappedClass)));
  }

}