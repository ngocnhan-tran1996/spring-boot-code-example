package com.springboot.code.example.database.support.oracle.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import oracle.jdbc.OracleStruct;

public interface OracleMapper<T> {

  Struct toStruct(T source, Connection connection, String typeName) throws SQLException;

  T fromStruct(OracleStruct struct);

}