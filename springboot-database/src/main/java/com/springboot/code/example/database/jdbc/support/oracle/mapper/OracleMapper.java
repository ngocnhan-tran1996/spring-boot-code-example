package com.springboot.code.example.database.jdbc.support.oracle.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;

public interface OracleMapper {

  <T> Struct toStruct(T source, Connection connection, String typeName) throws SQLException;

}
