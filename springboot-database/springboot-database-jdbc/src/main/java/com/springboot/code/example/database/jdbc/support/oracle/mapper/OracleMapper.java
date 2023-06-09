package com.springboot.code.example.database.jdbc.support.oracle.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import org.springframework.jdbc.core.RowMapper;

public interface OracleMapper<T> extends RowMapper<T> {

  Struct toStruct(T source, Connection connection, String typeName) throws SQLException;

}
