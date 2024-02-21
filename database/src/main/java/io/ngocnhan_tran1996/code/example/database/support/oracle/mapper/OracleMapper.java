package io.ngocnhan_tran1996.code.example.database.support.oracle.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Map;

public interface OracleMapper<T> {

    Struct toStruct(Connection connection, T source, String typeName) throws SQLException;

    T fromStruct(Connection connection, Struct struct);

    T convert(Map<String, Object> source);

}