package io.ngocnhan_tran1996.code.example.database.support.oracle.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Map;
import oracle.jdbc.OracleStruct;

public interface OracleMapper<T> {

  Struct toStruct(T source, Connection connection, String typeName) throws SQLException;

  T fromStruct(OracleStruct struct);

  T convert(Map<String, Object> source);

}