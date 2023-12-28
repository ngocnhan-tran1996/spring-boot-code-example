package com.springboot.code.example.database.support.oracle.mapper;

import oracle.jdbc.OracleStruct;

public interface OracleMapper<T> {

  // Struct toStruct(T source, Connection connection, String typeName) throws SQLException;

  T fromStruct(OracleStruct struct);

}